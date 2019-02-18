import React, {Component} from 'react';
import NewUserForm from "./NewUserForm";
import axios from "axios";
import ModalError from "../UI/ModalError";
import ModalMessage from "../UI/ModalMessage"
import $ from "jquery";

class UserAdminisrationList extends Component {
    state = {
        userBeingEdited: {
            firstname: "",
            lastname: "",
            username: "",
            userGroups: []
        }, // naudotojo, kuri siuo metu redaguojame, duomenys
        userlist: [], // masyvas visu surastu pagal paieska naudotoju
        searchField: '',
        allgroups: [],
        modalMessageText: ''
    }

    handleChangeInput = (event) => this.setState({[event.target.name]: event.target.value});
    //handleChangeSelect = (event) => this.setState({[event.target.name]: event.target.options[event.target.selectedIndex].value});

    componentDidMount() {
        this.getAllGroupsfromServer()
    }

    getFilteredUsers = (event) => {
        document.getElementById('userListTable').style.visibility = 'visible';
        event.preventDefault();
        axios.get(
            '/api/users/criteria', {
                params: {
                    criteria: this.state.searchField
                }
            })
            .then(response => {
                if (response.data.length > 0) {
                    this.setState({userlist: response.data});
                } else (window.alert("Pagal paieska nerasta "))
            })
            .catch(error => {
                console.log("Atsakymas is getFilteredUsers: " + error)
            });
    }

    getAllGroupsfromServer = () => {
        axios.get("/api/usergroups")
            .then(response => {
                    if (response.data.length > 0) {
                        this.setState({allgroups: response.data});
                    }
                }
            )
    }

    addUserToGroup = (username, groupTitle) => {
        axios.put('/api/usergroups/' + groupTitle + '/add-person', null, {
            params: {
                username: username
            }
        })
            .then(response => {
                this.loadUserToEdit(this.state.userBeingEdited.username);
            })
            .catch(error => {
                console.log("Error from removeUserFromGroup - " + error)
            })
    }

    removeUserFromGroup = (username, groupTitle) => {
        axios.put('/api/usergroups/' + groupTitle + '/remove-person', null, {
            params: {
                username: username
            }
        })
            .then(response => {
                this.loadUserToEdit(this.state.userBeingEdited.username);
            })
            .catch(error => {
                console.log("Error from removeUserFromGroup - " + error)
            })
    }

    deleteUser = (user) => {
        axios.delete('/api/users/' + user.username)
            .then(response => {
                let newUserlist = this.state.userlist.filter(u => u.username !== user.username)
                this.setState({userlist: newUserlist});
            })
            .catch(error => {
                console.log("Error from deleteUser - " + error)
            })

    }

    handleChangeUser = (user) => {
        this.loadUserToEdit(user.username);
        document.getElementById('editUserForm').style.visibility = 'visible';

    }

    loadUserToEdit = (username) => {
        if (username !== "") {
            axios.get('/api/users/' + username)
                .then(response => {
                        this.setState({userBeingEdited: response.data});
                    }
                )
        }
    }

    render() {
        return (
            <React.Fragment>
                <div className="container-fluid">
                    <h4 className="my-4" align="center">
                        Naudotojų administravimas
                    </h4>


                    <div className="form-group col-md-8 my-5">
                        <label htmlFor="exampleFormControlInput1">Naudotojo paieška</label>
                        <div className="row">
                            <div className="col-md-8 input-group">
                                <div className="input-group-prepend">
                                    <span className="input-group-text" id="basic-addon1">🔎</span>
                                </div>
                                <input className="form-control mr-sm-2" type="search"
                                       placeholder="Įveskite naudotojo vardą, pavardę arba registracijos vardą (username)"
                                       aria-label="Search" aria-describedby="basic-addon1"
                                       value={this.state.searchField}
                                       name="searchField"
                                       onChange={this.handleChangeInput}/>
                            </div>
                            <div className="col-md-2">
                                <button className="btn btn-info my-2 my-sm-0" type="submit"
                                        onClick={this.getFilteredUsers}>Ieškoti
                                </button>
                            </div>
                            <div className="col-md-2">
                                <button className="btn btn-outline-info my-2 my-sm-0 buttonXL" type="submit"
                                        onClick={() => {
                                            this.props.history.push("/user-registration")
                                        }}>Registruoti naują naudotoją
                                </button>
                            </div>
                        </div>
                    </div>


                    <table className="table table-bordered table-hover table-sm" id="userListTable"
                           style={{'visibility': 'hidden'}}>
                        <thead>
                        <th>Naudotojo vardas</th>
                        <th>Vardas</th>
                        <th>Pavardė</th>
                        <th>Naudotojo&nbsp;grupės</th>
                        <th>Veiksmai</th>
                        </thead>
                        <tbody>
                        {this.state.userlist.map(user => (
                            <tr key={user.userIdentifier}>
                                <td>{user.username}</td>
                                <td>{user.firstname}</td>
                                <td>{user.lastname}</td>
                                <td>
                                    {user.userGroups.map((group, index) =>
                                        <span
                                            key={index}>{group.title} {index < user.userGroups.length - 1 ? '|' : ''} </span>)}
                                </td>
                                <td>
                                    <button className="btn btn-info btn-sm"
                                            onClick={() => this.handleChangeUser(user)}>Redaguoti
                                    </button>
                                    <button className="btn btn-secondary btn-sm ml-2"
                                            onClick={() => this.deleteUser(user)}>Trinti
                                    </button>
                                </td>
                            </tr>
                        ))}
                        <tr>
                        </tr>

                        </tbody>
                    </table>

                    <div className='row' id="editUserForm" style={{'visibility': 'hidden'}}>
                        <div className="col-md-6">
                            <NewUserForm editmode={true}

                                         userIdentifier={this.state.userBeingEdited.userIdentifier}
                                         firstname={this.state.userBeingEdited.firstname}
                                         lastname={this.state.userBeingEdited.lastname}
                                         username={this.state.userBeingEdited.username}

                            />
                        </div>

                        <div className="col-md-6">
                            <h4 className="my-4" align="center">Naudotojo grupės</h4>
                            <table className="table">
                                <thead>
                                <tr>
                                    <th style={{width: '45%'}}>Priklauso</th>
                                    <th>&nbsp;</th>
                                    <th style={{width: '45%'}}>Nepriklauso</th>
                                </tr>
                                </thead>
                                <tbody>
                                {this.state.allgroups.map((group, index) =>
                                    this.state.userBeingEdited.userGroups.map(group => group.title).indexOf(group.title) > -1 ?
                                        <tr>
                                            <td>{group.title}</td>
                                            <td><a href="#"
                                                   onClick={() => this.removeUserFromGroup(this.state.userBeingEdited.username, group.title)}>▶</a>
                                            </td>
                                            <td></td>
                                        </tr>
                                        :
                                        <tr>
                                            <td></td>
                                            <td><a href="#"
                                                   onClick={() => this.addUserToGroup(this.state.userBeingEdited.username, group.title)}>◀</a>
                                            </td>
                                            <td>{group.title}</td>
                                        </tr>
                                )}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </React.Fragment>
        );
    }

}


/*{...this.state.userBeingEdited}*/

export default UserAdminisrationList;