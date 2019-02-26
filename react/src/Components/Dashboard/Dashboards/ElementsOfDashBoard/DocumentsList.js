import React from 'react';
import {Link} from 'react-router-dom';
// import {Button} from 'react-native';
import DateWithTooltip from "../../../UI/DateWithTooltip";

const AugustasDocumentsList = (props) => {
    return (


        <React.Fragment>
            <table className="table">
                <thead style={{backgroundColor: '#EEEEEE'}}>
                <tr>
                    <th>Dokumento pavadinimas</th>
                    <th>Autorius</th>
                    <th>Pateikimo data</th>
                    <th>Dokumento tipas</th>
                    <th className="text-center">Veiksmas</th>
                </tr>
                </thead>
                <tbody>
                {props.list.map(item => (
                    <tr key={item.documentIdentifier}>
                    <td>{item.title}</td>
                    <td>{item.author}</td>
                    <td><DateWithTooltip date={item.postedDate}/></td>
                    <td>{item.type}</td>

                    <td><Link className="btn btn-outline-info btn-block" to=
                    {"/documents/" + item.documentIdentifier}>Detaliau</Link></td>

                    </tr>
                ))}
                </tbody>
                {/* <Button variant="primary" onClick= {doStufff}>
                    Primary</Button> */}


            </table>
        </React.Fragment>
    );
};

export default AugustasDocumentsList;
