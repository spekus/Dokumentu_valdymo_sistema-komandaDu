import React, {Component} from 'react';
import FileSaver from 'file-saver';
import axios from 'axios';

// run npm install file-saver --save

export default class FileUploader extends Component {

    state = {

        file: '',
        error: '',
        msg: '',
        // savedFileIdentifier: '',
        // savedDocumentIdentifier: '',
        type: 'Pirmas',
        title: '',
        description: '',
        availableTypes: []
    }


    handleChangeInput = (event) => this.setState({[event.target.name]: event.target.value});
    handleChangeSelect = (event) => this.setState({[event.target.name]: event.target.options[event.target.selectedIndex].value});


    componentWillMount(userId){
        this.getTypesFromServer();
    }

    getTypesFromServer = (userId) => {
        axios.get('/api/documentTypes')
            .then(result => {
                if (result.data.length > 0) {
                    this.setState({availableTypes: result.data});
                    this.setState({type: result.data[0]});
                    console.log("Atsakymas is getTypesFromServer -" + result.data);
                }
            })
            .catch(error => {
                console.log("Atsakymas is getTypesFromServer - " + error)
            })

    }


    uploadFile = (event) => {
        event.preventDefault();
        this.setState({error: '', msg: ''});

        if (!this.state.file) {
            this.setState({error: 'Please upload a file.'})
            return;
        }

        if (this.state.file.size >= 2000000) {
            this.setState({error: 'File size exceeds limit of 2MB.'})
            return;
        }

        let data = new FormData();
        data.append('file', this.state.file);
        data.append('name', this.state.file.name);


        axios.post('/api/files', data)
            .then(response => {
                this.setState({error: '', msg: 'Sucessfully uploaded file'});
                if (response.data.text) {
                    var fileId = response.data.text;

                    let documentDetails = {
                        title: this.state.title,
                        type: this.state.type,
                        description: this.state.description
                    };

                    // Jeigu pavyko sukelti faila, mes bandome vartotojo vardu
                    // sukurti dokumento specifikacija
                    // ir suristi sukelta faila su juo
                    this.addDocument("id123", documentDetails, fileId);

                }
            })
            .catch(err => {
                this.setState({error: err.message})
                console.log("Error from /api/files - " + err)
            });


    }

    // Metodas prideda naudotojui dokumento specifikacija (DocumentDetails) ir susieja su failu,
    // kuris jau buvo ikeltas anksciau.
    addDocument(userId, documentDetails, fileId) {
        axios.post('/api/documents/' + userId + '/documentAddToGroups', documentDetails)
            .then(response => {
                this.setState({'type': '', 'title': '', 'description': ''});

                if (response.data.text) {
                    var docId = response.data.text;
                    this.addFileToDocument(docId, fileId);
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
        let fileDocumentCommand = {
            documentIdentifier: docId,
            fileIdentifier: fileID
        }

        axios.post('/api/files/addFileToDocument', fileDocumentCommand)
            .then(response => {
                this.setState({[this.state.name]: ''});
                console.log("Response from addFileToDocument - " + response)
            })
            .catch(err => {
                console.log("Error from /api/files/addFileToDocument - " + err)

            });
    }

    onFileChange = (event) => {
        this.setState({
            file: event.target.files[0]
        });
    }


    render() {
        return (
            <React.Fragment>

                {/* Main content */}
                <div>
                    <div>
                        <h4 className="my-4" align="center">
                            Naujo dokumento sukūrimas
                        </h4>


                        <form classname="form1 col-md-9" onSubmit={this.handleSubmit}>
                            <div className="row">
                                <div className="col-md-2"></div>
                                <div className="col-md-9">
                                    <div className="form-group col-md-10">
                                        <label htmlFor="exampleFormControlInput1">Pavadinimas</label>
                                        <input type="text" className="form-control" id="exampleFormControlInput1"
                                               placeholder="Įveskite dokumento pavadinimą" name="title"
                                               value={this.state.title}
                                               onChange={this.handleChangeInput}/>
                                    </div>
                                    <div className="form-group col-md-10">
                                        <label htmlFor="exampleFormControlSelect1">Dokumento tipas</label>
                                        <select className="form-control" id="exampleFormControlSelect1"
                                                value={this.state.type} onChange={this.handleChangeSelect} name="type">
                                            {this.state.availableTypes.map(item =>(
                                            <option value={item.title}>{item.title}</option>
                                            ))}

                                        </select>
                                    </div>
                                    <div className="form-group col-md-10">
                                        <label htmlFor="exampleFormControlTextarea1">Aprašymas</label>
                                        <textarea className="form-control" id="exampleFormControlTextarea1" rows="3"
                                                  placeholder="Įveskite trumpą dokumento aprašymą"
                                                  name="description"
                                                  value={this.state.description}
                                                  onChange={this.handleChangeInput}></textarea>
                                    </div>

                                    <div className="form-group col-md-9 mt-4">
                                        <input onChange={this.onFileChange} type="file"></input><br/>
                                        <h4 style={{color: 'red'}}>{this.state.error}</h4>
                                        <h4 style={{color: 'green'}}>{this.state.msg}</h4>

                                    </div>
                                </div>
                            </div>
                        </form>

                        <React.Fragment>

                            <div className="text-center">
                                <button type="submit" className="btn btn-danger my-4"
                                        onClick={this.uploadFile}>Išsaugoti
                                </button>
                            </div>

                        </React.Fragment>


                    </div>

                </div>
            </React.Fragment>
        );
    }
}


