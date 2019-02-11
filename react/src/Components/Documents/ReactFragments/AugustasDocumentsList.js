import React from 'react';
import {Link} from 'react-router-dom';
// import {Button} from 'react-native';

const AugustasDocumentsList = (props) => {
    // const doStufff = () => {
    //     console.log("HEEEELP")
    //     return (
    //     <div>
    // <h1>labas!!</h1>
    //     </div>
    //     )
    // }

    return (


        <React.Fragment>
            <table class="table">
                <thead style={{backgroundColor: '#EEEEEE'}}>
                <tr>
                    <th>Dokumento tipas</th>
                    <th>Autorius</th>
                    <th>Pateikimo data</th>
                    <th className="text-center">Detaliau</th>
                </tr>
                </thead>
                <tbody>
                {props.list.map(item => (
                    <tr key={item.documentIdentifier}>
                    <td>{item.title}</td>
                    <td>{item.author}</td>
                    <td>{item.postedDate}</td>

                    <td><Link className="btn btn-outline-info btn-block" to=
                    {"/documents/" + item.documentIdentifier}>Details</Link></td>

                    <td className='lastColumn'>{item.date}</td>
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
