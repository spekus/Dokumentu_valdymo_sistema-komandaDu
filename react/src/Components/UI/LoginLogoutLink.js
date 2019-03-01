import React from 'react';
import {NavLink} from "react-router-dom";

const LoginLogoutLink = (props) => {

    if (props.user === "") {
        return (
            <NavLink to='/login' className='navbar-brand'>
                Prisijungti
            </NavLink>);
    } else {
        return (
            <React.Fragment>
                <h5>
                    {props.user.firstname} {props.user.lastname} ({props.user.username})</h5>
                <NavLink to='/logout' className='navbar-brand'>
                    <i className='fa fa-sign-out-alt' style={{fontSize: '1.2em'}}/>
                </NavLink>
            </React.Fragment>
        );
    }
};

export default LoginLogoutLink;
