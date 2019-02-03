import React, {Component} from 'react';
import axios from 'axios';
import ModalError from "../UI/ModalError";
import $ from 'jquery';

class SettingsUserGroups extends Component {
    state = {
        newUserGroupInputField: "",
        allUserGroups: [],
        errorMessage: "",
        infoMessage: ""
    }

    componentDidMount() {
        this.getUserGroups();
    }

    handleChangeInput = (event) => this.setState({[event.target.name]: event.target.value});

    createUserGroup = (event) => {
        event.preventDefault();
        axios.post('/api/usergroup/', {title: this.state.newUserGroupInputField})
            .then(reponse => {
                    this.getUserGroups();
                    this.setState({newUserGroupInputField: ""})
                }
            );
    }

    getUserGroups = () => {
        axios.get('/api/usergroup/')
            .then(result => {
                this.setState({allUserGroups: result.data});
            })
            .catch(error => {
                console.log(error);
            })
    }

    deleteUserGroup = (userGroupTitle) => {
        axios.delete('/api/usergroup/' + userGroupTitle)
            .then(result => {
                this.getUserGroups();
            })
            .catch(error => {
                this.setState({errorMessage: error.response.data.message});
                $("#modalError").modal('show');
            })
    }

    render() {
        return (
            <div>
                <h5>Naudotojų grupės</h5>
                <div class="row">
                    <div className="col-md-8">
                        <table className="table table-bordered table-hover">
                            <thead>
                            <tr>
                                <th>Pavadinimas</th>
                                <th>Trinti</th>
                            </tr>
                            </thead>
                            <tbody>
                            {this.state.allUserGroups.map(group => (
                                <tr>
                                    <td>{group.title}</td>
                                    <td>
                                        <a href="#" className="text-danger" onClick={() => {
                                            this.deleteUserGroup(group.title)
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
                        <form onSubmit={this.createUserGroup}>
                            <div className="form-group">
                                <input type="text"
                                       className="form-control"
                                       placeholder="Nauja grupė"
                                       value={this.state.newUserGroupInputField}
                                       name="newUserGroupInputField"
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

export default SettingsUserGroups;