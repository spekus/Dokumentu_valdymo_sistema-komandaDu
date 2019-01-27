import React from 'react';
import {Route, BrowserRouter as Router, Switch} from 'react-router-dom'
import PropTypes from 'prop-types';
import classNames from 'classnames';
import {withStyles} from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import Drawer from '@material-ui/core/Drawer';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';
import Badge from '@material-ui/core/Badge';
import MenuIcon from '@material-ui/icons/Menu';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import NotificationsIcon from '@material-ui/icons/Notifications';
import {sideBarList} from './Components/UI/Menus/sideBarList';
import {style} from './Components/UI/Style/style';
import Dashboard from "./Components/Dashboard/Dashboard";
import UsersHome from "./Components/Users/UsersHome";
import ReportsHome from "./Components/Reports/ReportsHome";
import DocumentsHome from "./Components/Documents/DocumentsHome";
import NotFound from "./Components/UI/ServicePages/NotFound";
import FileUploader from "./Components/Files/FileUploader";


class App extends React.Component {
    state = {
        open: true,
    };

    handleDrawerOpen = () => {
        this.setState({open: true});
    };

    handleDrawerClose = () => {
        this.setState({open: false});
    };

    render() {
        const {classes} = this.props;


        return (
            <div className={classes.root}>
                <Router>
                    <React.Fragment>
                        <CssBaseline/>
                        <AppBar
                            position="absolute"
                            className={classNames(classes.appBar, this.state.open && classes.appBarShift)}
                        >
                            <Toolbar disableGutters={!this.state.open} className={classes.toolbar}>
                                <IconButton
                                    color="inherit"
                                    aria-label="Open drawer"
                                    onClick={this.handleDrawerOpen}
                                    className={classNames(
                                        classes.menuButton,
                                        this.state.open && classes.menuButtonHidden,
                                    )}
                                >
                                    <MenuIcon/>
                                </IconButton>
                                <Typography
                                    component="h1"
                                    variant="h6"
                                    color="inherit"
                                    noWrap
                                    className={classes.title}
                                >
                                    Dashboard
                                </Typography>
                                <IconButton color="inherit">
                                    <Badge badgeContent={4} color="secondary">
                                        <NotificationsIcon/>
                                    </Badge>
                                </IconButton>
                            </Toolbar>
                        </AppBar>
                        <Drawer
                            variant="permanent"
                            classes={{
                                paper: classNames(classes.drawerPaper, !this.state.open && classes.drawerPaperClose),
                            }}
                            open={this.state.open}
                        >
                            <div className={classes.toolbarIcon}>
                                <IconButton onClick={this.handleDrawerClose}>
                                    <ChevronLeftIcon/>
                                </IconButton>
                            </div>

                            {sideBarList}

                        </Drawer>
                        <main className={classes.content}>
                            <div className={this.props.classes.appBarSpacer}/>
                            <Switch>
                                <Route exact path="/" component={Dashboard}/>
                                <Route path="/documents" component={DocumentsHome}/>
                                <Route path="/users" component={UsersHome}/>
                                <Route path="/reports" component={ReportsHome}/>
                                <Route exact path="/upload-file" component={FileUploader}/>
                                <Route component={NotFound}/>
                            </Switch>
                        </main>
                    </React.Fragment>
                </Router>
            </div>
        );
    }
}

App.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(style)(App);