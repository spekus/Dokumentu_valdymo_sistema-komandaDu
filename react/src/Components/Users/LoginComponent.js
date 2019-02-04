import React, { Component } from 'react';
import axios from 'axios';
import './LoginComponent.css'
import { Redirect } from 'react-router-dom'

export default class Login extends Component{
    constructor(props){
        super(props);
        this.state={
            login:'',
            password:'',
            client:null,
            redirect:false
        };
    }

    axiosGet() {
        axios.get('/api/client/get/'+this.state.login)
            .then((response) => {
                this.setState({ client: response.data });
                console.log(this.state.client);
                if (this.state.password!==this.state.client.password || this.state.client===null){
                    alert('your password or login is incorrect!!!');
                }
                else{

                    this.setState({redirect:true})
                    alert("it's ok!");
                    // this.context.history.push({
                    //     pathname: '/user',
                    //     search: '?the=query',
                    //     state: { client: this.state.client }
                    //   })
                }
            })
            .catch((error) => {
                console.log(error);
            });
    }

    handleChange1=(event)=> {
        this.setState({login: event.target.value});
    }

    handleChange2=(event)=> {
        this.setState({password: event.target.value});
    }

    handleSubmit=(event)=> {
        event.preventDefault();
        this.axiosGet();
        // this.props.handleLoginComponent({});
    }


    renderRedirect = () => {
        if (this.state.redirect) {
            return (<Redirect to={{ pathname:'/user', state: { client: this.state.client}}} />)
        }
    }

    render(){
        console.log(this.state.client);
        return(
                <div className="login">
                    <h4>Login to Your DMS Account</h4><br/>
                    <form>
                        <input type="text" value={this.state.login} onChange={this.handleChange1}/><br/>
                        <input type="password" value={this.state.password} onChange={this.handleChange2}/><br/>
                        {/*<input type="submit" value="Login" onClick={this.handleSubmit}/>*/}
                        <button type="submit" value="Login" className="btn btn-danger my-2 " onClick={this.handleSubmit}>Prisijungti</button>
                    </form>
                    {this.renderRedirect()}
                </div>
        )
    }
}