import React, {Component} from 'react';
import axios from 'axios';
import ModalError from "../UI/ModalError";
import SettingsGroupsTypes from "./SettingsGroupsTypes"
import $ from 'jquery';
import ModalContainer from "../UI/ModalContainer";
import SettingsEditGroupTypes from "./SettingsEditGroupTypes";

class SettingsUserGroups extends Component {
    state = {
        newUserGroupInputField: "",
        // allUserGroups: [],
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

    // componentDidMount() {
    //     this.getUserGroups();
    // }

    handleChangeInput = (event) => this.setState({[event.target.name]: event.target.value});

    createUserGroup = (event) => {
        event.preventDefault();
        axios.post('/api/usergroups', {title: this.state.newUserGroupInputField, role: 'ROLE_USER'})
            .then(reponse => {
                    this.getAllGroupsFromServer();
                    this.setState({newUserGroupInputField: ""})
                }
            );
    }

    // getUserGroups = () => {
    //     axios.get('/api/usergroups')
    //         .then(result => {
    //             this.setState({allUserGroups: result.data});
    //         })
    //         .catch(error => {
    //             console.log(error);
    //         })
    // }
    //
    // deleteUserGroup = (userGroupTitle) => {
    //     axios.delete('/api/usergroups/' + userGroupTitle)
    //         .then(result => {
    //             this.getUserGroups();
    //         })
    //         .catch(error => {
    //             this.setState({errorMessage: error.response.data.message});
    //             $("#modalError").modal('show');
    //         })
    // }




       componentDidMount() {
        this.getAllGroupsFromServer();
        this.getAllTypes();
    }

    handleChangeSelect = (event) => this.setState({[event.target.name]: event.target.options[event.target.selectedIndex].value});

    getAllGroupsFromServer = () => {
        axios.get("/api/usergroups")
            .then(response => {
                if (response.data.length > 0) {
                    let allgroups = response.data;
                    this.setState({allgroups: allgroups});

                    if (this.state.groupBeingEdited.title != '')
                    {
                       this.setState({groupBeingEdited: allgroups.find((g) => g.title === this.state.groupBeingEdited.title )})
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
                    this.setState({allTypes: allTypes});
                    // this.setState({type: result.data[0].title});
                    console.log("allTypes - " + allTypes);
                }
            })
            .catch(error => {
                console.log("Error from /api/document-types - " + error)
            })
    }


    editGroupTitle = (group) => {
        console.log("Group to edit " + group.title);
        let newTitle = window.prompt("Įveskite naują grupės " + group.title + " pavadinimą");
        axios.post("/api/usergroups/" + group.title, null, {params: {newTitle: newTitle}})
            .then(response => {
                this.getAllGroupsFromServer();
            })
            .catch(error => {
                console.log("Error from removeTypefromGroup" + error.message)
            })
    }


    deleteGroup = (group) => {
        console.log("Group to remove " + group.title);
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
        this.setState({groupBeingEdited: group});
        // document.getElementById('editGroupTypes').style.visibility = 'visible';
        $('#typeModal').modal('show');
        this.getAllTypes();
        $('#typeModal').on('hidden.bs.modal', this.getAllGroupsFromServer)
    }



    render() {
        return (
            <div className='p-3 mb-5 bg-white rounded'>
                <h5>Naudotojų grupės</h5>
                <div class="row">
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
                                        <tr>
                                            <th>
                                                {group.title}
                                            </th>

                                            <td>


                                                {group.typesToUpload.map(type => (
                                                    <div className="row">
                                                        <div className="col-md-10">
                                                            <li>{type.title}</li>
                                                        </div>
                                                        <div className="col-md-2">

                                                        </div>

                                                    </div>
                                                ))}

                                            </td>
                                            <td>


                                                {group.typesToApprove.map(type => (
                                                    <div className="row">
                                                        <div className="col-md-9">
                                                            <li>{type.title}</li>
                                                        </div>
                                                        <div className="col-md-3">

                                                        </div>
                                                    </div>


                                                ))}


                                            </td>
                                            <td>
                                                <i className="fas fa-edit mr-3" title="Koreguoti grupės pavadinimą"
                                                   onClick={() => this.editGroupTitle(group)}> </i>

                                                <i className="fas fa-tasks mr-3" title="Redaguoti tipus"
                                                   onClick={() => this.editGroup(group)}
                                                   group={group}></i>


                                                <i className="fas fa-trash-alt" title="Ištrinti grupę"
                                                   onClick={() => this.deleteGroup(group)}></i>

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
                                                            onChange={this.getAllGroupsFromServer}/>
                                </ModalContainer>
                            </div>


                            {/*</div>*/}
                        </div>
                        {/*<table className="table table-bordered table-hover">*/}
                        {/*<thead>*/}
                        {/*<tr>*/}
                        {/*<th>Pavadinimas</th>*/}
                        {/*<th>Trinti</th>*/}
                        {/*</tr>*/}
                        {/*</thead>*/}
                        {/*<tbody>*/}
                        {/*{this.state.allUserGroups.map(group => (*/}
                        {/*<tr>*/}
                        {/*<td>{group.title}</td>*/}
                        {/*<td>*/}
                        {/*<a href="#" className="text-danger" onClick={() => {*/}
                        {/*this.deleteUserGroup(group.title)*/}
                        {/*}}>*/}
                        {/*X*/}
                        {/*</a></td>*/}
                        {/*</tr>*/}
                        {/*))*/}
                        {/*}*/}
                        {/*</tbody>*/}
                        {/*</table>*/}
                    </div>
                    <div className="col-md-4">
                        <form onSubmit={this.createUserGroup}>
                            <div className="form-group">
                                <input type="text"
                                       className="form-control"
                                       placeholder="Nauja grupė"
                                       value={this.state.newUserGroupInputField}
                                       name="newUserGroupInputField"
                                       onChange={this.handleChangeInput}/>
                                <button type="submit" className="btn btn-info mt-2">Sukurti</button>
                            </div>

                        </form>
                    </div>
                </div>

                <ModalError errorText={this.state.errorMessage}/>
            </div>
        );
    }
}

export default SettingsUserGroups;