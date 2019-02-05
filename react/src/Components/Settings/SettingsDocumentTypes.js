import React, {Component} from 'react';
import axios from 'axios'
import $ from "jquery";
import ModalError from "../UI/ModalError";

class SettingsDocumentTypes extends Component {
    state = {
        infoMessage: '',
        errorMessage: '',
        allDocumentTypes: [],
        newDocumentTypeInputField: ''
    }


    componentDidMount() {
        this.getAllDocumentTypes();
    }

    handleChangeInput = (event) => this.setState({[event.target.name]: event.target.value});



    createDocumentType = (event) => {
        // var newType = this.state.newDocumentTypeInputField;
        event.preventDefault();
        axios.post('/api/documentTypes/', {title: this.state.newDocumentTypeInputField})
            .then(response =>{
                this.getAllDocumentTypes();
                this.setState({newDocumentTypeInputField: ''});
            })
            .catch(error =>{
                console.log("Klaida is createDocumentType: " + error);
            })

    }

    getAllDocumentTypes = () => {
        axios.get('/api/documentTypes/')
            .then(result =>{
              this.setState({allDocumentTypes: result.data});
            })
            .catch(error=>{
                console.log("Klaida is getAllDocumentTypes: " + error.response.data.message);

            })
    }

    deleteDocumentType =(title) => {
        axios.delete('/api/documentTypes/'+ title)
            .then(result =>{
                this.getAllDocumentTypes();
            })
            .catch(error =>{
                console.log("Klaida is getAllDocumentTypes: " + error);
                $("#modalError").modal('show');
            })

    }



    render() {
        return (
            <div>
                <h5>Dokumentų tipų nustatymas</h5>
                <div className="row">
                    <div className="col-md-8">
                        <table className="table table-bordered table-hover">
                            <thead>
                            <tr>
                                <th>Pavadinimas</th>
                                <th>Trinti</th>
                            </tr>
                            </thead>
                            <tbody>
                            {this.state.allDocumentTypes.map(item => (
                                <tr>
                                    <td>{item.title}</td>
                                    <td>
                                        <a href="#" className="text-danger" onClick={() => {
                                            this.deleteDocumentType(item.title)
                                        }}>
                                            X
                                        </a></td>
                                </tr>
                            ))
                            }
                            </tbody>
                        </table>
                    </div>
                    <div className="col-md-4">
                        <form onSubmit={this.createDocumentType}>
                            <div className="form-group">
                                <input type="text"
                                       className="form-control"
                                       placeholder="Naujas dokumentų tipas"
                                       value={this.state.newDocumentTypeInputField}
                                       name="newDocumentTypeInputField"
                                       onChange={this.handleChangeInput}/>
                                <button type="submit" className="btn btn-primary mt-2">Sukurti</button>
                            </div>

                        </form>
                    </div>
                </div>

                <ModalError errorText={this.state.errorMessage}/>
            </div>
        );
    }
}

export default SettingsDocumentTypes;