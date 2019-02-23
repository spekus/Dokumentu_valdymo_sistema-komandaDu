import React, {Component} from 'react';
import axios from "axios";

class SettingsGroupsTypes extends Component {
    state = {
        allgroups: [],
        alltypes: [],
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
                    this.setState({alltypes: response.data});
                    // this.setState({type: result.data[0].title});
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


                                        {/*<td>*/}

                                        {/*<form>*/}
                                        {/*<div className="form-row align-items-center">*/}
                                        {/*<div className="col-auto my-1">*/}
                                        {/*<label className="mr-sm-2 sr-only"*/}
                                        {/*htmlFor="inlineFormCustomSelect">Preference</label>*/}
                                        {/*<select className="custom-select mr-sm-2"*/}
                                        {/*id="inlineFormCustomSelect" value={this.state.type}*/}
                                        {/*name="type" onChange={this.handleChangeSelect}>*/}
                                        {/*<option selected>Choose...</option>*/}
                                        {/*{this.state.alltypes.map(item => (*/}
                                        {/*<option value={item.title}>{item.title}</option>*/}
                                        {/*))}*/}
                                        {/*</select>*/}
                                        {/*</div>*/}

                                        {/*<div className="col-auto my-1">*/}
                                        {/*<button type="submit" className="btn btn-primary"*/}
                                        {/*onClick={this.addTypeToGroup}*/}
                                        {/*>Pridėti*/}
                                        {/*</button>*/}
                                        {/*</div>*/}
                                        {/*</div>*/}
                                        {/*</form>*/}

                                        {/*</td>*/}
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


                                </tr>

                            )
                        )}
                        </tbody>
                    </table>
                    <i className="fa fa-trash-o" aria-hidden="true"></i>

                </div>
            </div>
        );
    }
}

export default SettingsGroupsTypes;