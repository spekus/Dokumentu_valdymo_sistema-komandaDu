import React, {Component} from 'react';
import NewUserForm from "./NewUserForm";
import axios from "axios";

class UserAdminisrationList extends Component {
    state = {
        userBeingEdited: {}, // naudotojo, kuri siuo metu redaguojame, duomenys
        userlist: [], // masyvas visu surastu pagal paieska naudotoju
        searchField: ''
    }


    getFilteredUsers = () => {

// vietoj sito axios geto /api/users
        // turi atsirasti uzklausa i paieskos endpointa
        // i kuria reikia paduoti paiesko laukeli
        axios.get('/api/users/')
            .then(response => {
                if (response.data.length > 0) {
                    this.setState({userlist: response.data});
                } else (window.alert("Pagal paieska nerasta "))
            })
            .catch(error => {
                console.log("Atsakymas is getUserByUserIdentifier: " + error)
            });

    }


    handleChangeUser = (user) => {
        this.setState({userBeingEdited: user});
        //document.getElementById('editUserForm').style.visibility = 'visible';
        //window.alert("TO DO: user edit")
    }
    handleChangeUserHide = (event) => {
        document.getElementById('editUserForm').style.visibility = 'hidden';
    }

    render() {
        return (
            <React.Fragment>
                <div>
                    <h4 className="my-4" align="center">
                        Vartotojų administravimas 2.
                    </h4>

                    <button className="btn btn-danger my-2 my-sm-0" type="submit"
                            onClick={this.getFilteredUsers}>Ieškoti
                    </button>

                    <table>
                        <tbody>
                        {this.state.userlist.map(user => (
                            <tr>
                                <td>{user.username}</td>
                                <td>{user.firstname}</td>
                                <td>{user.lastname}</td>
                                <td>
                                    <button onClick={() => this.handleChangeUser(user)}>Edit</button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>

                    <div id="editUserForm">
                        <NewUserForm editmode={true}
                                     {...this.state.userBeingEdited}

                        />
                    </div>

                </div>
            </React.Fragment>
        );
    }
}

export default UserAdminisrationList;