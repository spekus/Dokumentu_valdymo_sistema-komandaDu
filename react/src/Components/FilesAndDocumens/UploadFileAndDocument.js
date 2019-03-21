import React, {Component} from 'react';

import axios from 'axios';
import '../../App.css';
import {showErrorObject} from "../../Components/UI/MainModalError";


export default class FileUploader extends Component {

    state = {

        files: [],
        error: '',
        msg: '',
        type: '',
        title: '',
        description: '',
        availableTypes: [],
    }


    handleChangeInput = (event) => this.setState({[event.target.name]: event.target.value});
    handleChangeSelect = (event) => this.setState({[event.target.name]: event.target.options[event.target.selectedIndex].value});

    componentWillMount() {
        this.getAllowedTypes();
    }

    validation() {

    }

    getAllowedTypes = () => {
        axios.get('/api/users/user/document-types')
            .then(result => {
                if (result.data.length > 0) {
                    this.setState({availableTypes: result.data});
                    this.setState({type: result.data[0].title});
                }
            })
            .catch(error => {
                console.log("Atsakymas is /api/users/user/document-types - " + error)
            })
    }

    // sleep = (ms) => {
    //     return new Promise(resolve => setTimeout(resolve, ms));
    // }

    handleSubmit = (event) => {
        console.log("handleSubmit");
        event.preventDefault();
        this.setState({ error: '', msg: '' });
        var fileIdentifiers = [];

        if (this.state.files.length === 0 || this.state.files === undefined) {
            this.setState({error: 'Pasirinkite failą'})
            return;
        }    
        console.log("count of file this.state.files - " + this.state.files)
        console.log("we will print this.state.files")
        console.log(this.state.files);
        var promises = []
        this.state.files.forEach(file => {

            let data = new FormData();
            data.append('file', file);
            data.append('name', file.name);
  
            promises.push(
            axios.post('/api/files', data)
                .then(response => {


                    if (response.data.text) {
                        var fileId = response.data.text;
                        fileIdentifiers.push(fileId);
                        console.log("File identifiers length" + fileIdentifiers.length)
                    }
                })
                .catch(err => {
                    this.setState({error: err.message})

                    showErrorObject(err);
                }));
            })
        axios.all(promises).then( () => {
            console.log("files are uploaded and now will be add to documents")
                     
                            let documentDetails = {
                                title: this.state.title,
                                type: this.state.type,
                                description: this.state.description
                            };
                            console.log("document details bellow")
                            console.log(documentDetails);
                            this.addDocument(documentDetails, fileIdentifiers)

            this.setState({ files: [] })
    })
    }

    addDocument(documentDetails, fileIdentifiers) {
        console.log("running addDocument");

        axios.post('/api/documents', documentDetails)
            .then(response => {
                this.setState({'title': '', 'description': ''});

                if (response.data.text) {
                    var docId = response.data.text;
                    console.log("doc ID" + docId);
                    fileIdentifiers.forEach(fileId => {

                        console.log(fileId + " is add Document");

                        this.addFileToDocument(docId, fileId);
                        console.log("FileIdentifiersForEach");
                    })

                    console.log("Document has been created with identifier - "
                        + docId);
                }
            })
            .catch(err => {
                this.setState({error: err.message})
                console.log("Error from /api/documents/ - " + err)
            });
    }

    // Sis metodas kvieciamas, kai turime dokumento ID ir failo ID
    // Jis surisa backende esanti faila su naudotojo dokumento specifikacija
    addFileToDocument(docId, fileID) {

        console.log("addFileToDocument running");
        let fileDocumentCommand = {
            documentIdentifier: docId,
            fileIdentifier: fileID
        }
        console.log("addFileToDocument");
        console.log("docId" + docId);
        console.log("fileID" + fileID);

        axios.post('/api/files/addFileToDocument'
            , fileDocumentCommand)
            .then(response => {
                this.setState({[this.state.name]: ''});
                console.log("Response from addFileToDocument - " + response)
                console.log(" " + response.status)
                console.log(" " + response.statusText)


            })
            .catch(err => {
                console.log("Error from /api/files/addFileToDocument - " + err)

            });

        this.setState({error: '', msg: 'Dokumentas sukurtas sėkmingai'});
    }

    onFileChange = (event) => {
        console.log("fileCHange");
        this.setState({ error: '', msg: '' });
        if (event.target.files && event.target.files[0].size <= 10000000 && event.target.files[0].type==="application/pdf") {

            this.setState({files: [...this.state.files, event.target.files[0]]})
        } else if (event.target.files[0].type !== "application/pdf") {
            this.setState({error: 'Galima pridėti tik PDF dokumentus'})
        } else {
            this.setState({error: 'Failo dydis viršija 10MB'})
        }
    }

    removeFile = (event, index) => {
        event.preventDefault();
        var arrayCopy = [...this.state.files];
        arrayCopy.splice(index, 1);
        this.setState({files: arrayCopy});
    }


    render() {
        return (
            <React.Fragment>
                <div className="container">
                    <div className="page1 p-3 mb-5 bg-white mainelement borderMain">

                        <form className="form1 col-md-9" onSubmit={this.handleSubmit}>
                            <div className="row">
                                <div className="col-md-2"></div>
                                <div className="col-md-9">
                                    <div className="form-group col-md-10">
                                        <label htmlFor="titleInput">Pavadinimas</label>
                                        <input type="text" className="form-control" id="titleInput"
                                               minLength="3"
                                               maxLength="50"
                                               pattern="^([a-zA-ąĄčČęĘėĖįĮšŠųŪžŽ]+[,.]?[ ]?|[A-Za-z0-9]+['-]?)+$"
                                               title="Only letters and numbers should be provided!"
                                               placeholder="Įveskite dokumento pavadinimą" name="title"
                                               value={this.state.title}
                                               onChange={this.handleChangeInput} required/>
                                    </div>
                                    <div className="form-group col-md-10">
                                        <label htmlFor="typeInput">Dokumento tipas</label>
                                        <select className="form-control" id="typeInput"
                                                value={this.state.type} onChange={this.handleChangeSelect} name="type"
                                                required>
                                            {this.state.availableTypes
                                                .sort((a, b) => a.title.localeCompare(b.title))
                                                .map(item => (
                                                    <option value={item.title}>{item.title}</option>
                                                ))}

                                        </select>
                                    </div>
                                    <div className="form-group col-md-10">
                                        <label htmlFor="descInput">Aprašymas</label>
                                        <textarea className="form-control" id="descInput" rows="3"
                                                  minLength="8"
                                                  maxLength="255"
                                                  pattern="^([a-zA-ąĄčČęĘėĖįĮšŠųŪžŽ]+[,.]?[ ]?|[A-Za-z0-9]+['-]?)+$"
                                                  placeholder="Įveskite trumpą dokumento aprašymą"
                                                  name="description"
                                                  value={this.state.description}
                                                  onChange={this.handleChangeInput} required/>
                                    </div>

                                    <div className="form-group col-md-9 mt-4">

                                        <input onChange={this.onFileChange}
                                               multiple type="file" accept=".pdf"
                                            //to hide unwanted text
                                               style={{color: 'white'}}
                                        ></input><br/>

                                        <label

                                            style={{marginTop: '20px'}}> {this.state.files.length > 0 ? 'Pridėti failai:' : ''}  </label>
                                        <ul>
                                            {
                                                this.state.files.map((file, index) => (
                                                    <li key={index}>
                                                        {file === undefined ? '' : file.name}
                                                        <button className="border-0"
                                                                onClick={(event ) => this.removeFile(event, index)}

                                                                style={{
                                                                    color: 'red',
                                                                    marginLeft: '8px',
                                                                    fontWeight: 'bold'
                                                                }}>

                                                            X
                                                        </button>
                                                    </li>
                                                ))}

                                        </ul>
                                        <h4 style={{color: 'red'}}>{this.state.error}</h4>
                                        <h4 style={{color: 'green'}}>{this.state.msg}</h4>

                                    </div>

                                </div>
                            </div>
                            <div className="text-center">
                                <button type="submit" className="btn button1 my-4">Išsaugoti
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </React.Fragment>
        );
    }
}