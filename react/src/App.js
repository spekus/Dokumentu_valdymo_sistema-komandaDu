import React from 'react';
import './App.css';
import {Route, BrowserRouter as Router, NavLink, Switch} from 'react-router-dom'
import Dashboard from "./Components/Dashboard/Dashboard";
import UsersList from "./Components/Users/UsersList";
import UserProfile from "./Components/Users/UserProfile";
import DocumentsHome from "./Components/Documents/DocumentsHome";
import NotFound from "./Components/UI/ServicePages/NotFound";
import FileUploader from "./Components/Files/FileUploader";
import SideNav, {NavItem, NavIcon, NavText} from '@trendmicro/react-sidenav';
import '@trendmicro/react-sidenav/dist/react-sidenav.css';
import LoginLogoutLink from "./Components/UI/LoginLogoutLink";
import FileDownloader from "./Components/Files/FileDownloader";
import UserAdministration from "./Components/Users/UserAdministration";
import NewUserForm from "./Components/Users/NewUserForm";
import LoginComponent from "./Components/Users/LoginComponent";

class App extends React.Component {
    state = {
        sideBarIsOpen: false,
        appBarText: "DVS",
        username: "user1",
    };

    menuItems = [
        {iconClass: 'fa fw fa-home', path: '', text: 'Pradžia'},
        {iconClass: 'fa fw fa-id-card', path: 'profile', text: 'Profilis'},
        {iconClass: 'fa fw fa-list', path: 'documents', text: 'Dokumentai'},
        {iconClass: 'fa fw fa-cloud-upload-alt', path: 'upload-file', text: 'Įkelti'},
        {iconClass: 'fa fw fa-users', path: 'user-administration', text: 'Naudotojai'},
    ];

    sideBarToggled = (isOpen) => {
        this.setState({sideBarIsOpen: isOpen});
    };

    sideBarClicked = (selected, location, history) => {
        const to = '/' + selected;
        if (location.pathname !== to) {
            history.push(to);
        }
    }

    handleLogout = (history) => {
        window.alert("Viso gero");
        this.setState({username: ""});
        history.push("/");
        return ("");
    }

    handleLogin = (history) => {
        this.setState({username: "Neo"});
        history.push("/profile");
        return ("");
    }

    handleLoginComponent = (data) => {
        console.log("Handle login component. Wee need data, so we can set username from it.");
        console.log(data);
        this.setState({username: data.username})
    }


    render() {
        return (
            <div>
                <Router>
                    <Route render={({location, history}) => (
                        <React.Fragment>

                            <SideNav
                                onSelect={(selected) => {
                                    this.sideBarClicked(selected, location, history)
                                }}
                                onToggle={this.sideBarToggled}
                                expanded={this.state.sideBarIsOpen}
                            >
                                <SideNav.Toggle/>

                                <SideNav.Nav defaultSelected="">
                                    {this.menuItems.map((item) =>
                                        <NavItem eventKey={item.path} id={item.path}>
                                            <NavIcon>
                                                <i className={item.iconClass} style={{fontSize: '1.75em'}}/>
                                            </NavIcon>
                                            <NavText>
                                                {item.text}
                                            </NavText>
                                        </NavItem>)}
                                </SideNav.Nav>
                            </SideNav>


                            <main className={this.state.sideBarIsOpen ? 'open' : ''}>

                                <nav className="navbar navbar-expand-sm bg-light navbar-light justify-content-between">
                                    <NavLink to='/' className="navbar-brand">{this.state.appBarText}</NavLink>

                                    <LoginLogoutLink username={this.state.username}/>
                                </nav>

                                <div id='main-content'>
                                    {this.state.username == "" ?
                                        <LoginComponent onLogin={this.handleLoginComponent}/>
                                        :
                                        <Switch>
                                            <Route exact path="/" component={Dashboard}/>
                                            <Route path="/documents" component={DocumentsHome}/>
                                            <Route path="/profile" component={UserProfile}/>
                                            <Route path="/users" component={UsersList}/>
                                            <Route exact path="/upload-file" component={FileUploader}/>
                                            <Route exact path="/download-file" component={FileDownloader}/>
                                            <Route exact path="/user-administration" component={UserAdministration}/>
                                            {/*<Route exact path="/user-administration"*/}
                                                   {/*render={(props) => <UserAdministration {...props}  />}/>*/}
                                            <Route exact path="/user-registration" component={NewUserForm}/>
                                            <Route exact path="/logout" render={() => this.handleLogout(history)}/>
                                            <Route exact path="/login" render={() => this.handleLogin(history)}/>
                                            <Route component={NotFound}/>
                                        </Switch>
                                    }
                                </div>
                            </main>


                        </React.Fragment>
                    )}
                    />
                </Router>
            </div>
        );
    }
}


export default (App);