import React, {Component} from 'react';
import Button from '@material-ui/core/Button';
import Grid from "@material-ui/core/Grid/Grid";
import Typography from "@material-ui/core/Typography/Typography";
import Divider from "@material-ui/core/Divider/Divider";
import Input from "@material-ui/core/Input/Input";
import FileSaver from 'file-saver';
// run npm install file-saver --save


export default class FileUploader extends Component {

    state = {
        file: '',
        error: '',
        msg: ''
    }

    uploadFile = (event) => {
        event.preventDefault();
        this.setState({error: '', msg: ''});

        if (!this.state.file) {
            this.setState({error: 'Please upload a file.'})
            return;
        }

        if (this.state.file.size >= 2000000) {
            this.setState({error: 'File size exceeds limit of 2MB.'})
            return;
        }

        let data = new FormData();
        data.append('file', this.state.file);
        data.append('name', this.state.file.name);

        fetch('http://localhost:8181/api/files', {
            method: 'POST',
            body: data
        })
            .then(response => {
                this.setState({error: '', msg: 'Sucessfully uploaded file'});
            })
            .catch(err => {
                    this.setState({error: err.message})
                }
            );


    }



    onFileChange = (event) => {
        this.setState({
            file: event.target.files[0]
        });
    }

    downloadFile = () => {
        fetch("http://localhost:8181/api/files/download/" + "4-uzduoties-egzamine-pvz.pdf")
        .then(response => {
          console.log(response);
          console.log("downloadRandomStuff");
        // Log somewhat to show that the browser actually exposes the custom HTTP header
        const fileNameHeader = "x-suggested-filename";
        const suggestedFileName = response.headers[fileNameHeader];
        const effectiveFileName = (suggestedFileName === undefined
                    ? "document.txt"
                    : suggestedFileName);
        console.log("Received header [" + fileNameHeader + "]: " + suggestedFileName
                    + ", effective fileName: " + effectiveFileName);
    
        // Let the user save the file.
        FileSaver.saveAs(response.url, effectiveFileName);
    
        }).catch((response) => {
            console.error("Could not Download the Excel report from the backend.", response);
        });
      }

    render() {
        return (
            <React.Fragment>
                <Grid container spacing={40}>
                    {/* Main content */}
                    <Grid item xs={12} md={6}>
                        <Typography variant="h6" gutterBottom>
                            Upload a file
                        </Typography>
                        <Divider/>
                        <h4 style={{color: 'red'}}>{this.state.error}</h4>
                        <h4 style={{color: 'green'}}>{this.state.msg}</h4>
                        <Input onChange={this.onFileChange} type="file"></Input><br/>

                        <Button onClick={this.uploadFile}>Upload</Button>
                    </Grid>

                    <Grid item xs={12} md={6}>
                        <Typography variant="h6" gutterBottom>
                            Download
                        </Typography>
                        <Divider/>
                        <Button onClick={this.downloadFile}>Download a random file</Button>
                    </Grid>

                </Grid>
            </React.Fragment>
        );
    }
}


