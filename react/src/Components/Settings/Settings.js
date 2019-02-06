import React, {Component} from 'react';
import SettingsDocumentTypes from "./SettingsDocumentTypes";
import SettingsUserGroups from "./SettingsUserGroups";

class Settings extends Component {
    render() {
        return (
            <div>
                <h3>Dokumentai</h3>
                <SettingsDocumentTypes/>
                <h3>Naudotojai</h3>
                <SettingsUserGroups/>
            </div>
        );
    }
}

export default Settings;