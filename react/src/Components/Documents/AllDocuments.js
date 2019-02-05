import React from 'react';

const AllDocuments = (props) => {
    return (
        <React.Fragment>
            <table class="table">
                <thead style={{backgroundColor: '#EEEEEE'}}>
                <tr>
                    <th>Dokumento tipas</th>
                    <th>Autorius</th>
                    <th className='lastColumn'>Sukurimo data</th>
                </tr>
                </thead>
                <tbody>
                {props.list.map(item => (
                    <tr key={item.id}>
                    <td>{item.doctype}</td>
                    <td>{item.author}</td>
                    <td className='lastColumn'>{item.date}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </React.Fragment>
    );
};

export default AllDocuments;
