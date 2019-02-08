import React from 'react';
import {Link} from 'react-router-dom'

const AugustasDocumentsList = (props) => {
    return (


        <React.Fragment>
            
            <table class="table">
                <thead style={{backgroundColor: '#EEEEEE'}}>
                <tr>
                    <th>Dokumento tipas</th>
                    <th>Autorius</th>
                    <th>Sukurimo data</th>
                    <th className='lastColumn'>Detaliau</th>
                </tr>
                </thead>
                <tbody>
                {props.list.map(item => (
                    <tr key={item.id}>
                    <td>{item.title}</td>
                    <td>{item.author}</td>
                    <td>{item.postedDate}</td>
                    <td><Link className="btn btn-outline-info btn-block" to=
                    {"/documents/" + item.documentIdentifier}>Details</Link></td>

                    <td className='lastColumn'>{item.date}</td>
                    </tr>
                ))}
                </tbody>


            </table>
        </React.Fragment>
    );
};

export default AugustasDocumentsList;
