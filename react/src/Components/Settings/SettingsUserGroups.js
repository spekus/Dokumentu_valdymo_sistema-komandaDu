import React, { Component } from 'react';
import axios from 'axios';
import ModalError from "../UI/ModalError";
import $ from 'jquery';
import ModalContainer from "../UI/ModalContainer";
import SettingsEditGroupTypes from "./SettingsEditGroupTypes";
import uuid from "uuid";
import "../../App.css"

class SettingsUserGroups extends Component {
    state = {
        newUserGroupInputField: "",
        errorMessage: "",
        infoMessage: "",

        allgroups: [],
        allTypes: [],
        groupBeingEdited: {
            title: '',
            typesToUpload: [],
            typesToApprove: [],
        },
        type: ''
    }



    handleChangeInput = (event) => this.setState({ [event.target.name]: event.target.value });

    createUserGroup = (event) => {
        event.preventDefault();
        axios.post('/api/usergroups', { title: this.state.newUserGroupInputField, role: 'ROLE_USER' })
            .then(reponse => {
                this.getAllGroupsFromServer();
                this.setState({ newUserGroupInputField: "" })
            }
            );
    }

    componentDidMount() {
        this.getAllGroupsFromServer();
        this.getAllTypes();
    }

    handleChangeSelect = (event) => this.setState({ [event.target.name]: event.target.options[event.target.selectedIndex].value });

    getAllGroupsFromServer = () => {
        axios.get("/api/usergroups")
            .then(response => {
                if (response.data.length > 0) {
                    let allgroups = response.data;
                    this.setState({ allgroups: allgroups });

                    if (this.state.groupBeingEdited.title !== '') {
                        this.setState({ groupBeingEdited: allgroups.find((g) => g.title === this.state.groupBeingEdited.title) })
                    }
                }
            })
            .catch(error => {
                console.log("Error from getAllGroupsFromServer: " + error.message)
            })
    }


    getAllTypes = () => {
        axios.get('/api/document-types')
            .then(response => {
                if (response.data.length > 0) {
                    let allTypes = response.data;
                    allTypes.sort((a, b) => a.title.localeCompare(b.title));
                    this.setState({ allTypes: allTypes });
                }
            })
            .catch(error => {
                console.log("Error from /api/document-types - " + error)
            })
    }


    editGroupTitle = (group) => {

        let newTitle = window.prompt("Įveskite naują grupės " + group.title + " pavadinimą");
        axios.post("/api/usergroups/" + group.title, null, { params: { newTitle: newTitle } })
            .then(response => {
                this.getAllGroupsFromServer();
            })
            .catch(error => {
                console.log("Error from removeTypefromGroup" + error.message)
            })
    }


    deleteGroup = (group) => {

        axios.delete('/api/usergroups/' + group.title)
            .then(response => {
                this.getAllGroupsFromServer();
                window.alert("Grupė " + group.title + " sėkmingai ištrinta")
            })
            .catch(error => {
                console.log("Error from deleteGroup" + error.message)
            })
    }

    editGroup = (group) => {
        this.setState({ groupBeingEdited: group });

        $('#typeModal').modal('show');
        this.getAllTypes();
        $('#typeModal').on('hidden.bs.modal', this.getAllGroupsFromServer)
    }



    render() {
        return (
            <div className='p-3 mb-5 bg-white rounded borderMain'>

                <div className="row">
                    <div className="col-md-8">
                        <div>
                            <table className="table table-hover table-bordered table-sm">
                                <thead>
                                    <tr>
                                        <th>Naudotojų grupė</th>
                                        <th>Dokumentų tipai kūrimui</th>
                                        <th>Dokumentų tipai tvirtinimui</th>
                                        <th>Veiksmai</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {this.state.allgroups.map(group => (
                                        <tr key={uuid()}>
                                            <th>
                                                {group.title}
                                            </th>

                                            <td>


                                                {group.typesToUpload
                                                    .sort((a, b) => a.title.localeCompare(b.title))
                                                    .map(type => (
                                                        <div className="row" key={uuid()}>
                                                            <div className="col-md-10">
                                                                <li>{type.title}</li>
                                                            </div>
                                                            <div className="col-md-2">

                                                            </div>

                                                        </div>
                                                    ))}

                                            </td>
                                            <td>


                                                {group.typesToApprove
                                                    .sort((a, b) => a.title.localeCompare(b.title))
                                                    .map(type => (
                                                        <div className="row" key={uuid()}>
                                                            <div className="col-md-9">
                                                                <li>{type.title}</li>
                                                            </div>
                                                            <div className="col-md-3">

                                                            </div>
                                                        </div>


                                                    ))}


                                            </td>
                                            <td className='settingstd'>
                                                <i className="fas fa-edit mr-3 " title="Koreguoti grupės pavadinimą"
                                                    onClick={() => this.editGroupTitle(group)}> </i>

                                                <i className="fas fa-tasks mr-3 " title="Redaguoti tipus"
                                                    onClick={() => this.editGroup(group)}
                                                    group={group}></i>


                                            </td>
                                        </tr>
                                    )
                                    )}
                                </tbody>
                            </table>


                            <div>
                                <ModalContainer id='typeModal'>
                                    <SettingsEditGroupTypes group={this.state.groupBeingEdited}
                                        alltypes={this.state.allTypes}
                                        onChange={this.getAllGroupsFromServer} />
                                </ModalContainer>
                            </div>


                            {/*</div>*/}
                        </div>

                    </div>
                    <div className="col-md-4">
                        <form onSubmit={this.createUserGroup}>
                            <div className="form-group">
                                <input type="text"
                                    className="form-control"
                                    placeholder="Nauja grupė"
                                    value={this.state.newUserGroupInputField}
                                    name="newUserGroupInputField"
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

export default SettingsUserGroups;