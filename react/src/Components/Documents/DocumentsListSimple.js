import React from 'react';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';


const DocumentsListSimple = (props) => {
    return (
        <React.Fragment>
            <Table>
                <TableHead style={{backgroundColor:'#EEEEEE'}}>
                    <TableRow>
                        <TableCell>Dokumento tipas</TableCell>
                        <TableCell align="right">Autorius</TableCell>
                        <TableCell align="right">Sukurimo data</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {props.list.map(item => (
                        <TableRow key={item.id}>
                            <TableCell component="th" scope="row">
                                {item.doctype}
                            </TableCell>
                            <TableCell align="right">{item.author}</TableCell>
                            <TableCell align="right">{item.date}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </React.Fragment>
    );
};

export default DocumentsListSimple;
