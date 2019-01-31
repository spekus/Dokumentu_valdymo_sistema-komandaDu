import React from 'react';
import {NavLink} from "react-router-dom";

const LoginLogoutLink = (props) => {

    if (props.username.length == 0) {
        return (
            <NavLink to='/login' className='navbar-brand'>
                Prisijungti
            </NavLink>);
    } else {
        return (
            <NavLink to='/logout' className='navbar-brand'>
                <i className='fa fa-sign-out-alt' style={{fontSize: '1.2em'}}/>
                Atsijungti
            </NavLink>
        );
    }
};

export default LoginLogoutLink;
