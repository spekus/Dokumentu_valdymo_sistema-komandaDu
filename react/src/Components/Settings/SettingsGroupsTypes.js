import React, {Component} from 'react';
import axios from "axios";
import {Link} from "react-router-dom";
import SettingsEditGroupTypes from "./SettingsEditGroupTypes";
import $ from "jquery";
import ModalContainer from "../UI/ModalContainer";

class SettingsGroupsTypes extends Component {
    state = {
        allgroups: [],
        allTypes: [],
        groupBeingEdited: {
            title: '',
            typesToUpload: [],
            typesToApprove: [],
        },
        type: ''
    }

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
            <div className="container">
                <div>
                    <table className="table table-hover table-bordered table-sm">
                        <thead>
                        <tr>
                            <th>Vartotojų grupė</th>
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
                                <SettingsEditGroupTypes group={this.state.groupBeingEdited} alltypes={this.state.allTypes} onChange={this.getAllGroupsFromServer}/>
                            </ModalContainer>
                        </div>


                    {/*</div>*/}
                </div>
            </div>
        );
    }
}

export default SettingsGroupsTypes;