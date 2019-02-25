import React, {Component} from 'react';
import axios from "axios";
import {Link} from "react-router-dom";

class SettingsGroupsTypes extends Component {
    state = {
        allgroups: [],
        allTypes: [],
        groupBeingEdited: {
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
                    this.setState({allgroups: response.data});
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
                    this.setState({allTypes: allTypes });
                    // this.setState({type: result.data[0].title});
                    console.log("allTypes - " + this.allTypes);
                }
            })
            .catch(error => {
                console.log("Error from /api/document-types - " + error)
            })
    }

    addTypeToGroupForUpload = (group) => {
        console.log("Group for editing - " + group.title);
        let doctype = window.prompt("Iveskite dokumento tipą, kurį norite pridėti grupei " + group.title);
        axios.put('/api/usergroups/' + group.title + '/add-document-type-to-upload', null, {params: {documentTypeTitle: doctype}})
            .then(response => {
                this.getAllGroupsFromServer();
            })
            .catch(error => {
                console.log("Error from addTypeToGroupForUpload" + error.message)
            })
    }

    addTypeToGroupForApprove = (group) => {
        console.log("Group for editing - " + group.title);
        let doctype = window.prompt("Iveskite dokumento tipą, kurį norite pridėti grupei " + group.title);
        axios.put('/api/usergroups/' + group.title + '/add-document-type-to-approve', null, {params: {documentTypeTitle: doctype}})
            .then(response => {
                this.getAllGroupsFromServer();
            })
            .catch(error => {
                console.log("Error from addTypeToGroupForApprove" + error.message)
            })

    }


    removeTypeForUploadfromGroup = (group, type) => {
        axios.put("/api/usergroups/" + group.title + "/remove-document-type-to-upload", null, {params: {documentTypeTitle: type.title}})
            .then(response => {
                this.getAllGroupsFromServer();
                console.log(group.title + " group removed")
            })
            .catch(error => {
                console.log("Error from removeTypefromGroup" + error.message)
            })
    }


    removeTypeForApprovefromGroup = (group, type) => {
        axios.put("/api/usergroups/" + group.title + "/remove-document-type-to-approve", null, {params: {documentTypeTitle: type.title}})
            .then(response => {
                this.getAllGroupsFromServer();
                console.log(group.title + " group removed")
            })
            .catch(error => {
                console.log("Error from removeTypefromGroup" + error.message)
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
        document.getElementById('editGroupTypes').style.visibility = 'visible';
        this.getAllTypes();
    }


    render() {
        return (
            <div className="container">
                <div>
                    <table className="table table-hover table-bordered">
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


                                        <button type="submit" className="btn btn-link btn-sm btn-block"
                                                onClick={() => this.addTypeToGroupForUpload(group)}
                                        >Pridėti dokumento tipą kūrimui
                                        </button>

                                        {group.typesToUpload.map(type => (
                                            <div className="row">
                                                <div className="col-md-10">
                                                    <li>{type.title}</li>
                                                </div>
                                                <div className="col-md-2">
                                                    <button className="btn btn-link text-danger"
                                                            onClick={() => this.removeTypeForUploadfromGroup(group, type)}>
                                                        <i className="fa fa-ban" aria-hidden="true"></i>
                                                    </button>
                                                </div>

                                            </div>
                                        ))}

                                    </td>
                                    <td>

                                        <button type="submit" className="btn btn-link btn-sm btn-block"
                                                onClick={() => this.addTypeToGroupForApprove(group)}
                                        >Pridėti dokumento tipą patvirtinimui
                                        </button>

                                        {group.typesToApprove.map(type => (
                                            <div className="row">
                                                <div className="col-md-9">
                                                    <li>{type.title}</li>
                                                </div>
                                                <div className="col-md-3">
                                                    <button className="btn btn-link text-danger"
                                                            onClick={() => this.removeTypeForApprovefromGroup(group, type)}>
                                                        <i className="fa fa-ban" aria-hidden="true"></i>
                                                    </button>
                                                </div>
                                            </div>


                                        ))}


                                    </td>
                                    <td>
                                        <i className="fas fa-edit mr-3" title="Koreguoti grupę"
                                           onClick={() => this.editGroupTitle(group)}> </i>

                                        <i className="fas fa-tasks mr-3" title="Redaguoti tipus"
                                           onClick={() => this.editGroup(group)}></i>


                                        <i className="fas fa-trash-alt" title="Ištrinti grupę"
                                           onClick={() => this.deleteGroup(group)}></i>

                                    </td>
                                </tr>
                            )
                        )}
                        </tbody>
                    </table>

                    <div className='row' id="editGroupTypes" style={{'visibility': 'hidden'}}>

                        {/*<SettingsEditGroupTypes group={this.state.groupBeingEdited}*/}
                        {/*typesToUpload={this.state.groupBeingEdited.typesToUpload}*/}
                        {/*typesToApprove={this.state.groupBeingEdited.typesToApprove}*/}
                        {/*alltypes={this.state.alltypes}*/}
                        {/*/>*/}


                        <div>

                            <h5>Grupė {this.state.groupBeingEdited.title}</h5>
                            <table className="table">
                                <thead>
                                <tr>
                                    <th>Tipas</th>
                                    <th>Kurti</th>
                                    <th>Tvirtinti</th>
                                </tr>
                                </thead>
                                <tbody>

                                {this.state.allTypes.map((type, index) =>
                                    <tr key={index}>
                                        <td>{type.title}</td>
                                        <td>
                                            <input
                                                type="checkbox"
                                                checked={this.state.groupBeingEdited.typesToUpload.map(t => t.title).indexOf(type.title)
                                            > -1}/>
                                        </td>
                                        <td>
                                           <input
                                                type="checkbox"
                                                checked={this.state.groupBeingEdited.typesToApprove.map(t => t.title).indexOf(type.title)
                                            > -1}/>
                                        </td>
                                    </tr>
                                )}

                                </tbody>
                            </table>

                        </div>


                    </div>
                </div>
            </div>
        );
    }
}

export default SettingsGroupsTypes;