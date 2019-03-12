import React, {Component} from 'react';
// import FileSaver from 'file-saver';
import axios from 'axios';
import '../../App.css';



// run npm install file-saver --save

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

//     async uploadFiles(fileIdentifiers,files) {

//         files.forEach(file => {

//             let data = new FormData();
//             data.append('file', file);
//             data.append('name', file.name);

        
//         try {
//         axios.post('/api/files', data)
        
//         .then(response => {
            
//             console.log("Uploadina faila");
//             // this.getAllowedTypes();
//             this.setState({error: '', msg: 'Dokumentas sukurtas sėkmingai'});
//             if (response.data.text) {
//                 var fileId = response.data.text;
//                 fileIdentifiers.push(fileId);
//                 console.log("laba diena");
//                 console.log("File identifiers length" + fileIdentifiers.length);
//             }
        
//         }) }
//         catch(err) {
//             this.setState({error: err.message})
//             console.log("Error from /api/files - " + err)
//         };
//     })

// }

sleep=(ms) => {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

    handleSubmit= async (event) => {

        event.preventDefault();
        console.log("0 file_state: " + this.state.files.length);
        this.setState({error: '', msg: ''});
        this.setState({files: []})
        console.log("1 file_state: " + this.state.files.length);
     
        var fileIdentifiers = [];

        if (this.state.files.length === 0 || this.state.files === undefined) {
            this.setState({error: 'Pasirinkite failą'})
            return;
        }

        console.log("Pries for eacha failu handleSubmit");
        this.state.files.forEach(file => {

            let data = new FormData();
            data.append('file', file);
            data.append('name', file.name);



            axios.post('/api/files', data)
                .then(response => {

                    this.setState({error: '', msg: 'Dokumentas sukurtas sėkmingai'});
                    if (response.data.text) {
                        var fileId = response.data.text;
                        fileIdentifiers.push(fileId);

                        console.log("File identifiers length" + fileIdentifiers.length);
                        
                    }
                })
                .catch(err => {
                    this.setState({error: err.message})
                    console.log("Error from /api/files - " + err)
                });



        })

        let documentDetails = {
            title: this.state.title,
            type: this.state.type,
            description: this.state.description
        };

    while(fileIdentifiers.length===0) {
        await this.sleep(1);
        console.log("While cikle: " + fileIdentifiers.length);
    }
    this.addDocument(documentDetails,fileIdentifiers);

        
    }
    

    // Metodas prideda naudotojui dokumento specifikacija (DocumentDetails) ir susieja su failu,
    // kuris jau buvo ikeltas anksciau.
    //Kol kas lyk neveikia?? 
    addDocument(documentDetails, fileIdentifiers) {
        console.log("running addDocument");
        // console.log(this.state.type);
        // console.log("type is" +this.state.type.valueOf);
        // console.log("type is" +this.state.type.text);
        // console.log("type is" +this.state.type.title);
        axios.post('/api/documents', documentDetails)
            .then(response => {
                this.setState({'title': '', 'description': ''});
                //idejau sita , nes vel buvo bugas.
                // this.getAllowedTypes()
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
    }

    onFileChange = (event) => {

        if (event.target.files[0].size <= 10000000) {

        this.setState({files: [...this.state.files, event.target.files[0]]})
        } else {
            this.setState({error: 'Failo dydis viršija 10MB'})
        }
    }

    removeFile = (index) => {
        var arrayCopy = [...this.state.files];
        arrayCopy.splice(index, 1);
        this.setState({files: arrayCopy});
    }
    

    render() {
        return (
            <React.Fragment>
                <div className="container">
                    {/*<h4 className="my-4 page-header">*/}
                        {/*Naujo dokumento kūrimas*/}
                    {/*</h4>*/}
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
                                                value={this.state.type} onChange={this.handleChangeSelect} name="type" required>
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
                                        multiple type="file"
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
                                                        <button className="border-0" onClick={() => this.removeFile(index)}

                                                              style={{color: 'red', marginLeft: '8px', fontWeight: 'bold'}}>
                                                    {/*<i onClick={() => this.removeFile(index)}*/}
                                                            {/*className="fas fa-minus-circle"*/}
                                                            {/*style={{marginLeft: '8px'}}/>*/}
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