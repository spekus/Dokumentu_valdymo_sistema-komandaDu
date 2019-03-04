import React, {Component} from 'react';
import NewUserForm from "./NewUserForm";
import axios from "axios";
import $ from "jquery";
import ModalContainer from "../UI/ModalContainer";
import EditUserGroups from "./EditUserGroups";
// import "../Styles/UserAdministrationList.css"
import '../../App.css'

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

    getFilteredUsers = () => {
        document.getElementById('userListTable').style.visibility = 'visible';
        axios.get(
            '/api/users/criteria', {
                params: {
                    criteria: this.state.searchField
                }
            })
            .then(response => {
                if (response.data.length > 0) {
                    this.setState({userlist: response.data});
                } else (window.alert("Pagal paiešką nerasta "))
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


    suspendUser = (user) => {
        axios.put('/api/usergroups/suspend-user', null, {params:{username: user.username}})
            .then(response =>{
                this.getFilteredUsers();
            })
            .catch(error =>{
                console.log("Error from suspendUser - " + error)
            })
    }

    handleChangeUser = (user) => {
        this.loadUserToEdit(user.username);
        // document.getElementById('editUserForm').style.visibility = 'visible';
        $('#userEditModal').modal('show');
    }
    handleChangeUserGroup = (user) => {
        this.loadUserToEdit(user.username);
        // document.getElementById('editUserForm').style.visibility = 'visible';
        $('#editUserGroupsModal').modal('show');
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

    handleGroupsChanged = () => {
        this.loadUserToEdit(this.state.userBeingEdited.username);
        this.getFilteredUsers();
    }

    render() {
        return (
            <React.Fragment>
                <div className="container">
                    {/*<h4 className="my-4 mainUA">*/}
                    {/*Naudotojų administravimas*/}
                    {/*</h4>*/}
                    <div className='mainelement borderMain' style={{'width': '100%'}}>


                        <div className="form-group col-md-8 my-3">
                            <label>Naudotojo paieška</label>
                            <div className="row">
                                <div className="col-md-8 input-group">
                                    <div className="input-group-prepend">
                                        <span className="input-group-text" id="basic-addon1" role="img"
                                              aria-label="Search">🔎</span>
                                    </div>
                                    <input className="form-control mr-sm-2" type="search"
                                           placeholder="Įveskite naudotojo vardą, pavardę arba reg. vardą (username)"
                                        // onFocus="Įveskite naudotojo vardą, pavardę arba reg. vardą (username)"
                                           aria-label="Search" aria-describedby="basic-addon1"
                                           value={this.state.searchField}
                                           name="searchField"
                                           onChange={this.handleChangeInput}/>
                                </div>
                                <div className="col-md-2">
                                    <button className="btn button2 my-2 my-sm-0 button1" type="submit"
                                            onClick={this.getFilteredUsers}>Ieškoti
                                    </button>
                                </div>
                                <div className="col-md-2">
                                    <button className="btn btn-outline-info my-2 my-sm-0 buttonXL button1"
                                            type="submit"
                                            onClick={() => {
                                                // this.props.history.push("/user-registration")
                                                $('#newUserModal').modal('show');
                                            }}>Registruoti naują naudotoją
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="my-4 borderMain mainelement" id="userListTable"
                         style={{'visibility': 'hidden'}}>
                        <table className="table table-bordered table-hover table-sm">
                            <thead>
                            <tr>
                                <th>Naudotojo vardas</th>
                                <th>Vardas</th>
                                <th>Pavardė</th>
                                <th>Naudotojo&nbsp;grupės</th>
                                <th>Veiksmai</th>
                            </tr>
                            </thead>
                            <tbody>
                            {this.state.userlist.map(user => (
                                <tr key={user.username}>
                                    <td>{user.username}</td>
                                    <td>{user.firstname}</td>
                                    <td>{user.lastname}</td>
                                    <td>
                                        {user.userGroups.map((group, index) =>
                                            <span
                                                key={index}>{group.title} {index < user.userGroups.length - 1 ? '|' : ''} </span>)}
                                    </td>
                                    <td>
                                        <button className="btn button1 btn-sm"
                                                onClick={() => this.handleChangeUser(user)}>Redaguoti
                                        </button>
                                        <button className="btn button1 btn-sm ml-2"
                                                onClick={() => this.handleChangeUserGroup(user)}>Grupės
                                        </button>
                                        <button className="btn button1 btn-sm ml-2"
                                                onClick={() => this.suspendUser(user)}>Blokuoti
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            <tr>
                            </tr>

                            </tbody>
                        </table>
                    </div>

                    <ModalContainer id='userEditModal' title="Naudotojo redagavimas">
                        <NewUserForm editmode={true}

                                     userIdentifier={this.state.userBeingEdited.userIdentifier}
                                     firstname={this.state.userBeingEdited.firstname}
                                     lastname={this.state.userBeingEdited.lastname}
                                     username={this.state.userBeingEdited.username}

                                     onSubmit={() => {
                                         $('#userEditModal').modal('hide');
                                         this.getFilteredUsers();
                                     }}
                        />
                    </ModalContainer>


                    <ModalContainer id='newUserModal' title="Naudotojo registravimas">
                        <NewUserForm editmode={false}

                                     onSubmit={() => {
                                         $('#newUserModal').modal('hide');
                                     }}
                        />
                    </ModalContainer>


                    <ModalContainer id='editUserGroupsModal' title="Naudotojo grupės">
                        <EditUserGroups user={this.state.userBeingEdited}
                                        allgroups={this.state.allgroups}
                                        onGroupsChanged={this.handleGroupsChanged}/>
                    </ModalContainer>
                </div>
            </React.Fragment>
        );
    }

}


/*{...this.state.userBeingEdited}*/

export default UserAdminisrationList;