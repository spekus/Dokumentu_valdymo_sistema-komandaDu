import React, {Component} from 'react';
import NavLink from "react-router-dom/es/NavLink";
import {Link} from "react-router-dom";
import axios from 'axios';

class UserAdministration extends Component {
    state = {
        userIdentifier: '',
        username: '',
        firstname: '',
        lastname: '',
        password: '',
        usergroups: [],
        identifierInputField: ''
    }

    handleChangeInput = (event) => this.setState({[event.target.name]: event.target.value});


    getUserByUserIdentifier = (event) => {
        var userID = this.state.identifierInputField;
        event.preventDefault();
        axios.get('/api/users/' + userID)
            .then(response => {
                if (response.data != 0) {
                this.setState({...this.state, ...response.data});
                this.setState({identifierInputField: ''});
                } else (window.alert("Tokio userio nėra"))
            })
            .catch(error => {
                console.log("Atsakymas is getUserByUserIdentifier: " + error)
            })

    }

    handleChangeUser = (event) => {
        event.preventDefault();
        window.alert("TO DO: user edit")
    }

    render() {
        return (
            <React.Fragment>
                <div>
                    <h4 className="my-4" align="center">
                        Vartotojų administravimas
                    </h4>

                    <div className="form-group col-md-8 my-5">
                        <label htmlFor="exampleFormControlInput1">Vartotojo paieška</label>
                        <div className="row">
                            <div className="col-md-8 input-group">
                                <div className="input-group-prepend">
                                    <span className="input-group-text" id="basic-addon1">🔎</span>
                                </div>
                                <input className="form-control mr-sm-2" type="search"
                                       placeholder="Įveskite vartotojo identifikatorių"
                                       aria-label="Search" aria-describedby="basic-addon1"
                                    value={this.state.identifierInputField}
                                       name="identifierInputField"
                                       onChange={this.handleChangeInput}/>
                            </div>
                            <div className="col-md-2">
                                <button className="btn btn-danger my-2 my-sm-0" type="submit"
                                        onClick={this.getUserByUserIdentifier}>Ieškoti
                                </button>
                            </div>
                            <div className="col-md-2">
                                <button className="btn btn-outline-danger my-2 my-sm-0 buttonXL" type="submit"
                                        onClick={() => {
                                            this.props.history.push("/user-registration")
                                        }}>Registruoti naują vartotoją
                                </button>
                            </div>
                        </div>
                    </div>
                    {/*/!*Galima padaryti taip ka daro Link viduje. Rankiniu budu papushinti i History *!/*/}
                    {/*<button className="btn btn-outline-danger my-2 my-sm-0" onClick={()=>{this.props.history.push("/profile")}}>Jusu profilis</button><br/>*/}

                    {/*/!*NavLink - tai yra kaip Link, bet moka prideti active klase, priklausomai nuo kelio*!/*/}
                    {/*<NavLink to="/profile">Jusu profilis</NavLink><br/>*/}

                    {/*/!*reikia naudoti Link*!/*/}
                    {/*<Link to="/profile">Jusu profilis</Link>*/}

                </div>
                <div>

                    <table className="table table-bordered">

                        <tbody>
                        <tr>
                            <th style={{"width": "20%"}}>Vartotojo identifikatorius</th>
                            <td style={{"width": "50%"}}
                                name="userIdentifier"
                                value="userIdentifier"
                            >{this.state.userIdentifier}</td>
                            <td style={{"width": "10%"}}>
                                <button className="btn" onClick={this.handleChangeUser}><i className="fas fa-edit"></i>
                                </button>
                            </td>
                        </tr>
                        <tr>
                            <th style={{"width": "20%"}}>Username</th>
                            <td style={{"width": "50%"}}
                                name="username"
                                value="username">{this.state.username}</td>
                            <td style={{"width": "10%"}}>
                                <button className="btn" onClick={this.handleChangeUser}><i className="fas fa-edit"></i>
                                </button>
                            </td>
                        </tr>
                        <tr>
                            <th style={{"width": "20%"}}>Vardas</th>
                            <td style={{"width": "50%"}}
                                name="firstname"
                                value="firstname">{this.state.firstname}</td>
                            <td style={{"width": "10%"}}>
                                <button className="btn" onClick={this.handleChangeUser}><i className="fas fa-edit"></i>
                                </button>
                            </td>
                        </tr>
                        <tr>
                            <th style={{"width": "20%"}}>Pavardė</th>
                            <td style={{"width": "50%"}}
                                name="lastname"
                                value="lastname">{this.state.lastname}</td>
                            <td style={{"width": "10%"}}>
                                <button className="btn" onClick={this.handleChangeUser}><i className="fas fa-edit"></i>
                                </button>
                            </td>
                        </tr>
                        <tr>
                            <th style={{"width": "20%"}}>Slaptažodis</th>
                            <td style={{"width": "50%"}}
                                name="password"
                                value="password">{this.state.password}</td>
                            <td style={{"width": "10%"}}>
                                <button className="btn" onClick={this.handleChangeUser}><i className="fas fa-edit"></i>
                                </button>
                            </td>
                        </tr>
                        <tr>
                            <th style={{"width": "20%"}}>Vartotojo grupės</th>
                            <td style={{"width": "50%"}}
                                name="password"
                                value="password">{this.state.usergroups}</td>
                            <td style={{"width": "10%"}}>
                                <button className="btn" onClick={this.handleChangeUser}><i className="fas fa-edit"></i>
                                </button>
                            </td>
                        </tr>

                        </tbody>

                    </table>

                    <h5>TO DO: get user's group</h5>
                    <h5>TO DO: add user to group</h5>
                    <h5>TO DO: delete user</h5>

                </div>
            </React.Fragment>

        )
            ;
    }
}

export default UserAdministration;