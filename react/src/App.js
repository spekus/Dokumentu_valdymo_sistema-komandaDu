import React from 'react';
import './App.css';
import {Route, BrowserRouter as Router, NavLink, Switch} from 'react-router-dom'
import DocumentDetailed from "./Components/FilesAndDocumens/DokumentDetailed";
import UsersList from "./Components/Users/UsersList";
import UserProfile from "./Components/Users/UserProfile";
import NotFound from "./Components/UI/ServicePages/NotFound";
import FileUploader from "./Components/FilesAndDocumens/UploadFileAndDocument";
import SideNav, {NavItem, NavIcon, NavText} from '@trendmicro/react-sidenav';
import '@trendmicro/react-sidenav/dist/react-sidenav.css';
import LoginLogoutLink from "./Components/UI/LoginLogoutLink";
import NewUserForm from "./Components/Users/NewUserForm";
import LoginComponent from "./Components/Users/LoginComponent";
import Settings from "./Components/Settings/Settings";
import UserAdminisrationList from "./Components/Users/UserAdminisrationList";
import GenericDashBoard from "./Components/Dashboard/Dashboards/GenericDashBoard";
import ToApproveDashboard from "./Components/Dashboard/Dashboards/ToApproveDashboard";
import axios from "axios";
import {Redirect} from "react-router";


class App extends React.Component {
    state = {
        sideBarIsOpen: false,
        appBarText: "DVS",
        user: "",
    };

    menuItems = [
        {iconClass: 'fa fw fa-home', path: '', text: 'Pradžia'},
        {iconClass: 'fa fw fa-id-card', path: 'profile', text: 'Profilis'},
        {iconClass: 'fa fw fa-list', path: 'documents/all', text: 'Dokumentai'},
        {iconClass: 'fa fw fa-cloud-upload-alt', path: 'upload-file', text: 'Įkelti'},
        // {iconClass: 'fa fw fa-users', path: 'user-administration', text: 'Naudotojai'},
        {iconClass: 'fa fw fa-users', path: 'user-administration-list', text: 'Naudotojai '},
        {iconClass: 'fa fw fa-cogs', path: 'settings', text: 'Nustatymai'},
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

    // si funkcija kreipiasi o browseris panaudota cookie. tokiu budu naudojant salutini efekta mes
    // suzinosime ar esame prisijunge
    getWhoAmI = () => {
        axios.get('/api/users/whoami')
            .then(response => {
                if (response.data.username != null) {

                    let user = response.data;
                    let isAdmin = false;

                    user.userGroups.map(group => {
                        if (group.role === "ROLE_ADMIN") {
                            isAdmin = true;
                        }
                    })
                    user = {...user, isAdmin: isAdmin}
                    console.log(user)
                    this.setState({user: user});
                }
            })
            .catch(error => {
                console.log("Error getting user info from server");
                console.log(error);
            })
    }

    handleLogOut = () => {
        axios.get('/logout')
            .then(response => {
                console.log("Logout success");
                console.log(response.data);
                this.setState({user: ""})
            })
            .catch(error => {
                console.log("Logout error: ");
                console.log(error.data);
                this.setState({user: ""})
            })

        return (<Redirect to='/'/>);
    }

    componentDidMount() {
        this.getWhoAmI();
    }

    render() {
        return (
            <div>
                <Router>
                    <Route render={({location, history}) => (
                        <React.Fragment>

                            <SideNav id="mysidenav"
                                     onSelect={(selected) => {
                                         this.sideBarClicked(selected, location, history)
                                     }}
                                     onToggle={this.sideBarToggled}
                                     expanded={this.state.sideBarIsOpen}
                            >
                                <SideNav.Toggle/>

                                <SideNav.Nav defaultSelected="">
                                    {this.menuItems.map((item) =>
                                        <NavItem key={item.path} eventKey={item.path} id={item.path}>
                                            <NavIcon>
                                                <i className={item.iconClass} style={{fontSize: '1.75em'}}/>
                                            </NavIcon>
                                            <NavText>
                                                {item.text}
                                            </NavText>
                                        </NavItem>)}
                                </SideNav.Nav>
                            </SideNav>

                            <nav id="mainnavbar" className={this.state.sideBarIsOpen ?
                                'navbar navbar-expand-sm bg-light navbar-light justify-content-between open'
                                :
                                'navbar navbar-expand-sm bg-light navbar-light justify-content-between'}>
                                <NavLink to='/' className="navbar-brand">{this.state.appBarText}</NavLink>

                                <LoginLogoutLink user={this.state.user}/>
                            </nav>

                            <main className={this.state.sideBarIsOpen ? 'open' : ''}>


                                <div id='main-content'>
                                    {this.state.user === "" ?
                                        <LoginComponent onLogin={this.getWhoAmI}/>
                                        :
                                        <Switch>
                                            {/* <Route exact path="/" component={AugisDashBoard}/> */}
                                            <Redirect exact from='/' to='/dashboard/documents/all'/>
                                            <Route path="/dashboard/documents/to_aproove"
                                                   render={(props) => <ToApproveDashboard user={this.state.user} {...props}/>}/>
                                                {/*// component={ToApproveDashboard}/>*/}
                                            <Route path="/dashboard/documents/:id" render={(props) => <GenericDashBoard
                                                user={this.state.user} {...props}/>}/>
                                            <Route exact path="/documents/:id" render={(props) => <DocumentDetailed
                                                user={this.state.user} {...props}/>}/>
                                            {/* <Route path="/documents" component={DocumentsHome}/> */}
                                            <Route path="/profile" render={(props) => <UserProfile
                                                user={this.state.user} {...props}/>}/>
                                            <Route path="/users" component={UsersList}/>
                                            <Route exact path="/upload-file" render={(props) => <FileUploader
                                                user={this.state.user} {...props}/>}/>
                                            {/* <Route exact path="/download-file" component={FileDownloader}/> */}
                                            {/*<Route exact path="/user-administration" component={UserAdministration}/>*/}
                                            <Route exact path="/user-administration-list"
                                                   component={UserAdminisrationList}/>
                                            <Route path="/settings"
                                                   render={(props) => <Settings user={this.state.user} {...props}/>}/>
                                            {/*<Route exact path="/user-administration"*/}
                                            {/*render={(props) => <UserAdministration {...props}  />}/>*/}
                                            <Route exact path="/user-registration" component={NewUserForm}/>
                                            <Route exact path="/logout" render={() => this.handleLogOut()}/>
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