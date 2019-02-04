import React, {Component} from 'react';
import SettingsDocumentTypes from "./SettingsDocumentTypes";
import SettingsUserGroups from "./SettingsUserGroups";

class Settings extends Component {
    render() {
        return (
            <div>
                <h1>Dokumentai</h1>
                <SettingsDocumentTypes/>
                <h1>Naudotojai</h1>
                <SettingsUserGroups/>
            </div>
        );
    }
}

export default Settings;