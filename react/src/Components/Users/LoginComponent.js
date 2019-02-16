import React, {Component} from 'react';
import axios from 'axios';
import './LoginComponent.css';
import {Redirect} from 'react-router-dom';


export default class Login extends Component {
    constructor(props) {
        super(props);
        this.resetState();
    }

    tryLogin() {
        var params = new URLSearchParams();
        params.append('username', this.state.username);
        params.append('password', this.state.password);


        axios.post('/login', params)
            .then(response => {
                // mes prisijungeme, todel dabar galime suzinoti naudotojo informacija
                this.props.onLogin(this.props.history);
            })
            .catch(error => {
                console.log("Error trying to log in. Dumping error: ");
                console.log(error);
                this.resetState();
            })
    }

       resetState() {
        this.state = {
            username: '',
            password: '',
            user: null,
            redirect: false
        };
    }


    handleChange1 = (event) => {
        this.setState({username: event.target.value});
    }

    handleChange2 = (event) => {
        this.setState({password: event.target.value});
    }

    handleSubmit = (event) => {
        event.preventDefault();
        this.tryLogin();
    }

    render() {
        return (
            <div className="username">
                <h4>Login to Your DMS Account</h4><br/>
                <form>
                    <input type="text" value={this.state.username} placeholder="Vartotojo vardas" onChange={this.handleChange1}/><br/>
                    <input type="password" value={this.state.password} placeholder="Slaptažodis" onChange={this.handleChange2}/><br/>
                    <button type="submit" value="username" className="btn btn-outline-info my-2 "
                            onClick={this.handleSubmit}>Prisijungti
                    </button>
                </form>
            </div>
        )
    }
}

