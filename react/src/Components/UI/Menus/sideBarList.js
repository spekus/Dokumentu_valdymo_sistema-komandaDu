import React from 'react';
import {Link as RouterLink} from "react-router-dom";
import Link from '@material-ui/core/Link';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import ListSubheader from '@material-ui/core/ListSubheader';
import DashboardIcon from '@material-ui/icons/Dashboard';
import AddBoxIcon from '@material-ui/icons/AddBox';
import ViewListIcon from '@material-ui/icons/ViewList'
import PeopleIcon from '@material-ui/icons/People';
import BarChartIcon from '@material-ui/icons/BarChart';
import InfoIcon from '@material-ui/icons/Info';
import Divider from "@material-ui/core/Divider/Divider";
import List from "@material-ui/core/List/List";

export const sideBarList = (
    <div>
        <Divider/>
        <List>
            <ListItem button component={RouterLink} to='/'>
                <ListItemIcon>
                    <DashboardIcon/>
                </ListItemIcon>
                <ListItemText primary="Dashboard"/>
            </ListItem>
            <ListItem button component={RouterLink} to='/documents'>
                <ListItemIcon>
                    <ViewListIcon/>
                </ListItemIcon>
                <ListItemText primary="Documents"/>
            </ListItem>
            <ListItem button component={RouterLink} to='/users'>
                <ListItemIcon>
                    <PeopleIcon/>
                </ListItemIcon>
                <ListItemText primary="Users"/>
            </ListItem>
            <ListItem button component={RouterLink} to='/reports'>
                <ListItemIcon>
                    <BarChartIcon/>
                </ListItemIcon>
                <ListItemText primary="Reports"/>
            </ListItem>
            <Divider/>
            <ListSubheader inset>Quick actions</ListSubheader>
            <ListItem button component={RouterLink} to='/upload-file'>
                <ListItemIcon>
                    <AddBoxIcon/>
                </ListItemIcon>
                <ListItemText primary="File upload demo"/>
            </ListItem>
            <Divider/>
            <ListSubheader inset>DEV links</ListSubheader>
            <ListItem button component={Link} href='https://material.io/tools/icons/?icon=view_list&style=baseline'
                      target='_blank'>
                <ListItemIcon>
                    <InfoIcon/>
                </ListItemIcon>
                <ListItemText primary="Material icons"/>
            </ListItem>
            <ListItem button component={Link} href='https://material-ui.com/style/typography/' target='_blank'>
                <ListItemIcon>
                    <InfoIcon/>
                </ListItemIcon>
                <ListItemText primary="Material typography"/>
            </ListItem>
        </List>
    </div>
);
