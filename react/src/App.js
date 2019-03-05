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
// import DownloadZip from "./Components/FilesAndDocumens/DownloadZip"
import SettingsGroupsTypes from "./Components/Settings/SettingsGroupsTypes"
import Spinner from "./Components/UI/Spinner";
import LocationToText from "./Components/UI/LocationToText";
import Charts from "./Components/Statistics/polarChart";
import MainModalError from './Components/UI/MainModalError';


class App extends React.Component {
    state = {
        sideBarIsOpen: false,
        appBarText: "DVS",
        user: "",
        loading: true // reiskia kad reikia rodyti Spinner elementa
    };

    menuItems = [
        {iconClass: 'fa fw fa-home', path: '', text: 'Pradžia'},
        {iconClass: 'fa fw fa-id-card', path: 'profile', text: 'Profilis'},
        {iconClass: 'fa fw fa-list', path: 'dashboard/documents/all', text: 'Dokumentai'},
        {iconClass: 'fa fw fa-cloud-upload-alt', path: 'upload-file', text: 'Įkelti'},
        {iconClass: 'fa fw fa-users', path: 'user-administration-list', text: 'Naudotojai', admin:true},
        {iconClass: 'fa fw fa-cogs', path: 'settings', text: 'Nustatymai', admin:true},
        {iconClass: 'far fa-chart-bar', path: 'statistics', text: 'Statistika'}
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
                if (response.data.username !== null) {

                    let user = response.data;
                    let isAdmin = false;

                    user.userGroups.forEach(group => {
                        if (group.role === "ROLE_ADMIN") {
                            isAdmin = true;
                        }
                    })
                    user = {...user, isAdmin: isAdmin}
                    this.setState({user: user});
                }
            })
            .catch(error => {
                console.log("Error getting user info from server");
                console.log(error);
            })
            .finally(() => {
                    // nesvarbu ar mes gavome klaida ar gera atsakyma, reikia nustoti rodyti <Spinner/>
                    // tam padarome loading = false
                    this.setState({loading: false})
                }
            )
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
                <MainModalError/>
                <Router>
                    <Route render={({location, history}) => (
                        this.state.user === "" ?
                            this.state.loading ? <Spinner/> : <LoginComponent onLogin={this.getWhoAmI}/> :
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
                                        this.state.user.isAdmin || !item.admin ?
                                        <NavItem key={item.path} eventKey={item.path} id={item.path}>
                                            <NavIcon>
                                                <i className={item.iconClass} style={{fontSize: '1.75em'}}/>
                                            </NavIcon>
                                            <NavText>
                                                {item.text}
                                            </NavText>
                                        </NavItem> : '' )}
                                </SideNav.Nav>
                            </SideNav>



                            <nav id="mainnavbar" className={this.state.sideBarIsOpen ?
                                'navbar navbar-expand-sm bg-light navbar-light justify-content-between open'
                                :
                                'navbar navbar-expand-sm bg-light navbar-light justify-content-between'}>
                                <NavLink to='/' className="navbar-brand" id="appbarText">{this.state.appBarText}</NavLink>
                                <LoginLogoutLink user={this.state.user}/>
                            </nav>

                            <main className={this.state.sideBarIsOpen ? 'open' : ''}>
                                <div id='main-content' id="pageContent">
                                    <div className="container">
                                        <div className="page-header pt-5">
                                            <h3><LocationToText location={location}/></h3>
                                        </div>
                                        <hr className="myhr"/>
                                    </div>
                                        <Switch>
                                            {/* <Route exact path="/" component={AugisDashBoard}/> */}
                                            <Redirect exact from='/' to='/dashboard/documents/all'/>
                                            <Route path="/dashboard/documents/to_aproove"
                                                   render={(props) => <ToApproveDashboard
                                                       user={this.state.user} {...props}/>}/>
                                            <Route path="/dashboard/documents/:id" render={(props) => <GenericDashBoard
                                                user={this.state.user} {...props}/>}/>
                                            {/*// component={ToApproveDashboard}/>*/}
                                            {/*<Route path="/dashboard/documents/test" render={(props) => <ToApproveDashboard*/}
                                                {/*user={this.state.user} {...props}/>}/>*/}
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
                                            <Route exact path="/settings-test"
                                                   render={(props) => <SettingsGroupsTypes
                                                       user={this.state.user} {...props}/>}/>

                                            <Route exact path="/user-registration" component={NewUserForm}/>
                                            <Route exact path="/logout" render={() => this.handleLogOut()}/>
                                            <Route exact path="/statistics" component={Charts}/>
                                            {/* <Route exact path="/zip"
                                                   render={(props) => <DownloadZip 
                                                   user={this.state.user} {...props}/>}/>
                                             */}
                                            <Route component={NotFound}/>

                                            {/*<Route  exact path="/settings-test"*/}
                                            {/*render={(props) => <SettingsGroupsTypes user={this.state.user} {...props}/>}/>*/}


                                        </Switch>

                                </div>
                            </main>

                            {/*<div className="footer">*/}
                            {/*<p>Footer</p>*/}
                            {/*</div>*/}

                        </React.Fragment>
                    )}
                    />
                </Router>
            </div>
        );
    }
}


export default (App);