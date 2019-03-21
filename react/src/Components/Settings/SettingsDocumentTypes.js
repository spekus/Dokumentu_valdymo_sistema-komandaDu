import React, { Component } from 'react';
import axios from 'axios'
import $ from "jquery";
import ModalError from "../UI/ModalError";
import uuid from "uuid";
import "../../App.css";
import { showErrorObject } from "../../Components/UI/MainModalError";

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

    handleChangeInput = (event) => this.setState({ [event.target.name]: event.target.value });


    createDocumentType = (event) => {
        event.preventDefault();
        var newType = { title: this.state.newDocumentTypeInputField }
        axios.post('/api/document-types', newType)
            .then(response => {
                this.getAllDocumentTypes();
                this.setState({ newDocumentTypeInputField: '' });
            })
            .catch(error => {
                showErrorObject(error);
            })

    }

    getAllDocumentTypes = () => {
        axios.get('/api/document-types')
            .then(result => {
                this.setState({ allDocumentTypes: result.data });
            })
            .catch(error => {
                showErrorObject(error);
            })
    }

    editDocumentTitle = (title) => {
        console.log("Type to edit " + title);
        let newTitle = window.prompt("Įveskite naują dokumento tipo " + title + " pavadinimą");
        let editedType = { title: newTitle };
        axios({
            method: 'put',
            url: '/api/document-types',
            params: {
                currentTitle: title,
            },
            data: editedType,
            headers: { 'Content-Type': 'application/json;charset=utf-8' }
        })
            .then(response => {
                this.getAllDocumentTypes();
            })
            .catch(error => {
                showErrorObject(error);
            })
    }


    deleteDocumentType = (title) => {
        axios.delete('/api/document-types/' + title)
            .then(result => {
                this.getAllDocumentTypes();
            })
            .catch(error => {
                showErrorObject(error);
            })
    }


    render() {
        return (
            <div className='p-3 mb-5 bg-white rounded borderMain'>
                <div className="row">
                    <div className="col-md-8">
                        <table className="table table-bordered table-hover">
                            <thead>
                                <tr>
                                    <th>Pavadinimas</th>
                                    <th>Redaguoti</th>
                                </tr>
                            </thead>
                            <tbody>
                                {this.state.allDocumentTypes
                                    .sort((a, b) => a.title.localeCompare(b.title))
                                    .map(item => (
                                        <tr key={uuid()}>
                                            <td>{item.title}</td>
                                            <td>

                                                <i className="fas fa-edit mr-3" id="iconsettings"
                                                    title="Koreguoti documento pavadinimą" onClick={() => {
                                                        this.editDocumentTitle(item.title)
                                                    }}> </i>
                                            </td>
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
                                    onChange={this.handleChangeInput} />
                                <button type="submit" className="btn button1 mt-2">Sukurti</button>
                            </div>

                        </form>
                    </div>
                </div>

                <ModalError errorText={this.state.errorMessage} />
            </div>
        );
    }
}

export default SettingsDocumentTypes;