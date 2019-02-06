import React, { Component } from 'react';
import axios from 'axios';
import './LoginComponent.css'
import { Redirect } from 'react-router-dom'

export default class Login extends Component {
    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
            user: null,
            redirect: false
        };
    }


    axiosGet() {
        axios({
            method: 'get',
            url: 'http://localhost:8181/api/users/login',
            params: {
                username: this.state.username,
                password: this.state.password
            },
            headers: {'Content-Type': 'application/json;charset=utf-8'}
        }).then((response) => {
            this.setState({user: response.data});
            console.log(this.state.user);


            if (this.state.user === null || this.state.user==='') {
                console.log(this.state.redirect);
            }
            else {
                this.setState({redirect: true})
                console.log(this.state.redirect);

            }
        })
            .catch((error) => {
                console.log(error);
            });
        }


        handleChange1 = (event) => {
            this.setState({username: event.target.value});
        }

        handleChange2 = (event) => {
            this.setState({password: event.target.value});
        }

        handleSubmit = (event) => {
            event.preventDefault();
            this.axiosGet();
        }


        renderRedirect = () => {
            if (this.state.redirect) {
                return (<Redirect to={{pathname: '/', state: {user: this.state.user}}}/>)
            }
        }

        render()
        {
            return (
                <div className="username">
                    <h4>Login to Your DMS Account</h4><br/>
                    <form>
                        <input type="text" value={this.state.username} onChange={this.handleChange1}/><br/>
                        <input type="password" value={this.state.password} onChange={this.handleChange2}/><br/>
                        <button type="submit" value="username" className="btn btn-danger my-2 "
                                onClick={this.handleSubmit}>Prisijungti
                        </button>
                    </form>
                    {this.renderRedirect()}
                </div>
            )
        }
    }

