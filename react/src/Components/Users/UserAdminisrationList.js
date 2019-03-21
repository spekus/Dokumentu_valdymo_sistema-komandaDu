import React, { Component } from 'react';
import NewUserForm from "./NewUserForm";
import axios from "axios";
import $ from "jquery";
import ModalContainer from "../UI/ModalContainer";
import EditUserGroups from "./EditUserGroups";
import '../../App.css'
import { showErrorObject } from "../UI/MainModalError";
import ReactPaginate from 'react-paginate';

class UserAdminisrationList extends Component {
    state = {
        userBeingEdited: {
            firstname: "",
            lastname: "",
            username: "",
            userGroups: []
        },
        userlist: [],
        searchField: '',
        lastSearchCriteria: '',
        allgroups: [],
        modalMessageText: '',


        pageCount: 3,
        perPage: 7,
        offset: 0,
        isPaginationOn: false
    }

    handleChangeInput = (event) => this.setState({ [event.target.name]: event.target.value });

    componentDidMount() {
        this.getAllGroupsfromServer()
    }

    getFilteredUsers = () => {
        this.setState({ isPaginationOn: true });

        if (this.state.searchField.length === 0) {
            this.setState({ userlist: [] })

            this.setState({ offset: 0 }, () => {

                this.loadAllUsers();

            })
        } else {
            this.setState({ userlist: [] })
            this.setState({ offset: 0 }, () => {
                this.getFilteredUsersByCriteria(this.state.searchField);
            })

        }
    }

    getFilteredUsersByCriteria = (criteria) => {


        this.setState({ offset: 0 });
        this.setState({ searchField: '' });
        this.setState({ lastSearchCriteria: criteria });

        document.getElementById('userListTable').style.visibility = 'visible';
        axios.get(
            '/api/users/criteria', {
                params: {
                    criteria: criteria,
                    page: this.state.offset,
                    size: this.state.perPage,

                }
            })
            .then(response => {

                if (response.data.content.length > 0) {

                    this.setState({ userlist: response.data.content });
                    this.setState({
                        pageCount:
                            Math.ceil(response.data.totalElements
                                / this.state.perPage)
                    })

                    let userlistExtended = this.state.userlist.map(user => {
                        let isSuspended = false;
                        user.userGroups.map(group => {

                            if (group.role === "ROLE_SUSPENDED") {
                                isSuspended = true;
                            }
                        })
                        let userExtended = { ...user, isSuspended: isSuspended };
                        return userExtended;

                    });
                    this.setState({ userlist: userlistExtended });


                } else (window.alert("Pagal paiešką nerasta "))
            })
            .catch(error => {
                console.log("Atsakymas is getFilteredUsers: " + error);
                showErrorObject(error);
            });
    }

    getAllGroupsfromServer = () => {
        axios.get("/api/usergroups")
            .then(response => {
                if (response.data.length > 0) {
                    this.setState({ allgroups: response.data });
                }
            }
            )
            .catch(error => {
                showErrorObject(error);
            })
    }


    deleteUser = (user) => {
        axios.delete('/api/users/' + user.username)
            .then(response => {
                let newUserlist = this.state.userlist.filter(u => u.username !== user.username)
                this.setState({ userlist: newUserlist });
            })
            .catch(error => {
                console.log("Error from deleteUser - " + error);
                showErrorObject(error);
            })

    }

    suspendUser = (user) => {
        axios.put('/api/usergroups/suspend-user', null, { params: { username: user.username } })
            .then(response => {
                this.getFilteredUsersByCriteria(user.username);
            })
            .catch(error => {
                console.log("Error from suspendUser - " + error)
                showErrorObject(error);
            })
    }


    removeUserFromGroup = (user) => {
        if (user.userGroups.length > 0) {
            let grouptitle = user.userGroups
                .filter(group => group.role === 'ROLE_SUSPENDED')
                .map(group => group.title);

            axios.put('/api/usergroups/' + grouptitle + '/remove-person', null, {
                params: {
                    username: user.username
                }
            })
                .then(response => {
                    this.getFilteredUsersByCriteria(user.username);
                })
                .catch(error => {
                    console.log("Error from removeUserFromGroup - " + error);
                    showErrorObject(error);
                })
        }
    }


    handleChangeUser = (user) => {
        this.loadUserToEdit(user.username);

        $('#userEditModal').modal('show');
    }
    handleChangeUserGroup = (user) => {
        this.loadUserToEdit(user.username);

        $('#editUserGroupsModal').modal('show');
    }

    handlePageClick = data => {
        let selected = data.selected;
        let offset = Math.ceil(selected);

        this.setState({ offset: offset }, () => {
            this.loadAllUsers();
        });
    };

    loadAllUsers = () => {

        axios({
            method: 'get',
            url: '/api/users',
            params: {
                page: this.state.offset,
                size: this.state.perPage,
            },
            headers: { 'Content-Type': 'application/json;charset=utf-8' }

        })

            .then(response => {
                document.getElementById('userListTable').style.visibility = 'visible';

                if (response.data.content.length > 0) {
                    this.setState({ userlist: response.data.content });


                    this.setState({
                        pageCount:
                            Math.ceil(response.data.totalElements
                                / this.state.perPage)
                    })

                }
            })
            .catch(error => {
                showErrorObject(error);
            })
    }


    loadUserToEdit = (username) => {
        if (username !== "") {
            axios.get('/api/users/' + username)
                .then(response => {
                    this.setState({ userBeingEdited: response.data });
                }
                )
                .catch(error => {
                    showErrorObject(error);
                })
        }
    }

    handleGroupsChanged = () => {
        this.loadUserToEdit(this.state.userBeingEdited.username);
        ;
        this.getFilteredUsersByCriteria(this.state.userBeingEdited.username);
    }

    handleKeyPress = (event) => {
        if (event.key === 'Enter') {
            this.setState({ [event.target.name]: event.target.value });
            this.getFilteredUsers();
        }
    }


    render() {
        return (
            <React.Fragment>
                <div className="container">

                    <div className='mainelement borderMain' style={{ 'width': '100%' }}>


                        <div className="form-group col-md-10 my-3">
                            <label>Naudotojo paieška</label>
                            <div className="row">
                                <div className="col-md-8 input-group">
                                    <div className="input-group-prepend">
                                        <span className="input-group-text" id="basic-addon1" role="img"
                                            aria-label="Search">🔎</span>
                                    </div>
                                    <input className="form-control mr-sm-2" type="search"
                                        placeholder="Įveskite naudotojo vardą, pavardę arba reg. vardą (username)"

                                        aria-label="Search" aria-describedby="basic-addon1"
                                        value={this.state.searchField}
                                        name="searchField"
                                        onChange={this.handleChangeInput}
                                        onKeyPress={this.handleKeyPress} />
                                </div>
                                <div className="col-md-1">
                                    <button className="btn button2 my-2 my-sm-0 button1" type="submit"
                                        onClick={this.getFilteredUsers}
                                    >Ieškoti
                                    </button>
                                </div>
                                <div className="col-md-2">
                                    <button className="btn btn-outline-info my--2 my-sm-0 buttonXL1 button1"
                                        type="submit"
                                        onClick={() => {

                                            $('#newUserModal').modal('show');
                                        }}>Registruoti naują naudotoją
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="my-4 borderMain mainelement" id="userListTable"
                        style={{ 'visibility': 'hidden' }}>
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
                                            {user.userGroups
                                                .map((group, index) =>
                                                    <span
                                                        key={index}>{group.title} {index < user.userGroups.length - 1 ? '|' : ''} </span>)}
                                        </td>
                                        <td>
                                            <span style={{ 'white-space': 'nowrap' }}>
                                                <button className="btn button1 btn-sm mr-2"
                                                    onClick={() => this.handleChangeUser(user)}>Redaguoti
                                        </button>
                                                <button className="btn button1 btn-sm mr-2"
                                                    onClick={() => this.handleChangeUserGroup(user)}>Grupės
                                        </button>
                                                {user.isSuspended ?
                                                    <button className="btn button1 btn-sm"
                                                        onClick={() => this.removeUserFromGroup(user)}>Atblokuoti
                                                </button>
                                                    :
                                                    <button className="btn button1 btn-sm"
                                                        onClick={() => this.suspendUser(user)}>Blokuoti
                                                </button>
                                                }
                                            </span>

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

                            afterSubmit={(username) => {
                                $('#userEditModal').modal('hide');
                                this.setState({ searchField: '' })

                                this.getFilteredUsersByCriteria(username);


                            }}
                        />
                    </ModalContainer>


                    <ModalContainer id='newUserModal' title="Naudotojo registravimas">
                        <NewUserForm editmode={false}

                            afterSubmit={(username) => {
                                $('#newUserModal').modal('hide');
                                this.getFilteredUsersByCriteria(username);
                            }}
                        />
                    </ModalContainer>


                    <ModalContainer id='editUserGroupsModal' title="Naudotojo grupės">
                        <EditUserGroups user={this.state.userBeingEdited}
                            allgroups={this.state.allgroups}
                            onGroupsChanged={this.handleGroupsChanged} />
                    </ModalContainer>

                    {this.state.isPaginationOn ?
                        <div>

                            <div className='container-fluid mt-2'>
                                <div className="row">
                                    <div className="col-lg-12 my-auto center-block text-center">

                                        <ReactPaginate
                                            forcePage={this.state.offset}
                                            previousLabel={'ankstesnis puslapis'}
                                            nextLabel={'kitas puslapis'}
                                            breakLabel={'...'}
                                            breakClassName={'break-me'}
                                            pageCount={this.state.pageCount}
                                            marginPagesDisplayed={2}
                                            pageRangeDisplayed={5}
                                            onPageChange={this.handlePageClick}
                                            containerClassName={'pagination'}
                                            subContainerClassName={'pagesPagination'}
                                            activeClassName={'active'}
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>
                        :
                        null}


                </div>

            </React.Fragment>
        );
    }

}




export default UserAdminisrationList;