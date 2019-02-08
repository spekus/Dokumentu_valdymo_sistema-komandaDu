import React, {Component} from 'react';
import NavLink from "react-router-dom/es/NavLink";
import {Link} from "react-router-dom";
import axios from 'axios';
import NewUserForm from "./NewUserForm";

class UserAdministration extends Component {
    constructor() {
        super();
        this.state = this.emptyState;
    }

    // state = {
    //     userIdentifier: '',
    //     username: '',
    //     firstname: '',
    //     lastname: '',
    //     password: '',
    //     usergroups: [],
    //     identifierInputField: '',
    //     group: '',
    //     availableGroups: []
    // }
    emptyState = {
        userIdentifier: '',
        username: '',
        firstname: '',
        lastname: '',
        password: '',
        usergroups: [],
        identifierInputField: '',
        group: '',
        availableGroups: [],
        //userlist:[],
    }


    handleChangeInput = (event) => this.setState({[event.target.name]: event.target.value});
    handleChangeSelect = (event) => this.setState({[event.target.name]: event.target.options[event.target.selectedIndex].value});


    getAllGroupsfromServer = (userID) => {
        axios.get("/api/usergroup/")
            .then(response => {
                    if (response.data.length > 0) {
                        this.setState({availableGroups: response.data});
                    } else {
                        (window.alert("Nėra sukurta jokių grupių"));
                        this.setState({availableGroups: ["Nėra sukurta jokių grupių", ""]})
                    }
                }
            )
    }

    getAllUserGroups = (userID) => {
        axios.get('/api/users/' + userID + '/usergroups')
            .then(response => {
                if (response.data.length > 0) {
                    this.setState({usergroups: response.data});
                } else {
                    (window.alert("Naudotojas nepriskirtas grupėms"));
                    this.setState({usergroups: ["Naudotojas nepriskirtas grupėms"]})
                }
            })
            .catch(error => {
                    console.log("Atsakymas is getUserByUserIdentifier getUserGroup: " + error)
                }
            )
    }

    getUserByUserIdentifier = (event) => {
        var userID = this.state.identifierInputField;
        event.preventDefault();
        axios.get('/api/users/' + userID)
            .then(response => {
                if (response.data != 0) {
                    this.setState({...this.state, ...response.data});
                    this.setState({identifierInputField: ''});
                } else (window.alert("Tokio naudotojo nėra"))
            })
            .catch(error => {
                console.log("Atsakymas is getUserByUserIdentifier: " + error)
            });

        this.getAllUserGroups(userID);

        this.getAllGroupsfromServer(userID);

    }


    addGroup = (event) => {
        var userID = this.state.userIdentifier;
        var newGroup = this.state.group;
        axios({
                method: 'put',
                url: '/api/usergroup/' + newGroup + '/add-person',
                params: {
                    userIdentifier: this.state.userIdentifier
                },
                headers: {'Content-Type': 'application/json;charset=utf-8'}
            }
        )

        // axios.put('/api/usergroup/' + newGroup + '/add-person', {userIdentifier:this.state.userIdentifier})
            .then(response => {
                this.getAllGroupsfromServer(userID);
                this.getAllUserGroups(userID);
            })
            .catch(error => {
                console.log("Error from addGroup - " + error)
            })
    }


    deleteUser = (userID) => {
        var userID = this.state.userIdentifier;
        var usernameToDelete = this.state.username;

        axios.delete('/api/users/' + userID)
            .then(response => {
                this.setState(this.emptyState);
                window.alert("Vartotojas" + usernameToDelete + "(vartotojo identifikatorius " + userID + " ) ištrintas")
            })
            .catch(error => {
                console.log("Error from deleteUser - " + error)
            })

    }

    handleChangeUser = (event) => {
        document.getElementById('editUserForm').style.visibility = 'visible';
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
                                       placeholder="Įveskite naudotojo identifikatorių"
                                       aria-label="Search" aria-describedby="basic-addon1"
                                       value={this.state.identifierInputField}
                                       name="identifierInputField"
                                       onChange={this.handleChangeInput}/>
                            </div>
                            <div className="col-md-2">
                                <button className="btn btn-info my-2 my-sm-0" type="submit"
                                        onClick={this.getUserByUserIdentifier}>Ieškoti
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
                    {/*/!*Galima padaryti taip ka daro Link viduje. Rankiniu budu papushinti i History *!/*/}
                    {/*<button className="btn btn-outline-danger my-2 my-sm-0" onClick={()=>{this.props.history.push("/profile")}}>Jusu profilis</button><br/>*/}

                    {/*/!*NavLink - tai yra kaip Link, bet moka prideti active klase, priklausomai nuo kelio*!/*/}
                    {/*<NavLink to="/profile">Jusu profilis</NavLink><br/>*/}

                    {/*reikia naudoti Link*/}
                    {/*<Link to="/profile">Jusu profilis</Link>*/}

                </div>
                <div>

                    <table className="table table-bordered">

                        <tbody>
                        <tr>
                            <th style={{"width": "20%"}}>Naudotojo identifikatorius</th>
                            <td style={{"width": "50%"}}
                                name="userIdentifier"
                                value="userIdentifier"
                            >{this.state.userIdentifier}</td>
                        </tr>
                        <tr>
                            <th style={{"width": "20%"}}>Prisijungimo vardas (username)</th>
                            <td style={{"width": "50%"}}
                                name="username"
                                value="username">{this.state.username}</td>
                        </tr>
                        <tr>
                            <th style={{"width": "20%"}}>Vardas</th>
                            <td style={{"width": "50%"}}
                                name="firstname"
                                value="firstname">{this.state.firstname}</td>
                        </tr>
                        <tr>
                            <th style={{"width": "20%"}}>Pavardė</th>
                            <td style={{"width": "50%"}}
                                name="lastname"
                                value="lastname">{this.state.lastname}</td>
                        </tr>
                        <tr>
                            <th style={{"width": "20%"}}>Slaptažodis</th>
                            <td style={{"width": "50%"}}
                                name="password"
                                value="password">{this.state.password}</td>
                        </tr>
                        <tr>
                            <th style={{"width": "20%"}}>Naudotojo grupės</th>
                            <td style={{"width": "50%"}}
                                name="usergroups"
                                value="usergroups">

                                {this.state.usergroups.map(item => (
                                    <span>{item.title} |  </span>
                                ))}

                            </td>
                        </tr>

                        </tbody>

                    </table>


                    <div>
                        <form>
                            <div className="form-group col-md-10">
                                <label htmlFor="exampleFormControlSelect1">Turimos grupės</label>

                                <select className="form-control" id="exampleFormControlSelect1"
                                        value={this.state.group} onChange={this.handleChangeSelect} name="group">
                                    {this.state.availableGroups.map(item => (
                                        <option value={item.title}>{item.title}</option>
                                    ))}
                                </select>
                            </div>
                        </form>
                    </div>

                    <div>
                        <button type="submit" className="btn btn-info my-1 mx-3"
                                onClick={this.addGroup}>Pridėti
                        </button>
                    </div>
                    <br/>
                    <br/>

                    <form className="form-inline mx-3 my-4">
                        <label className="sr-only" htmlFor="inlineFormInputName2">Name</label>
                        <input type="text" className="form-control mb-2 mr-sm-2" id="inlineFormInputName2"
                               value={this.state.userIdentifier} name="userIdentifier"/>


                        <button type="submit" className="btn btn-secondary mb-2" onClick={this.deleteUser}>Ištrinti
                            naudotoją
                        </button>
                    </form>

                </div>
                <button onClick={this.handleChangeUser}>Redaguoti</button>
                <button onClick={this.handleChangeUserHide}>Paslepti redagavimo forma</button>

                <div id="editUserForm" style={{'visibility': 'hidden'}}>
                    <NewUserForm editmode={true}
                                 userIdentifier={this.state.userIdentifier}
                                 firstname={this.state.firstname}
                                 lastname={this.state.lastname}
                                 username={this.state.username}

                    />
                </div>
            </React.Fragment>

        )
            ;
    }
}

export default UserAdministration;