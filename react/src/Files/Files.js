import React, { Component } from 'react';
import reactLogo from '../logo.svg';
import springBootLogo from '../spring-boot-logo.png';
import '../App.css';

class Files extends Component {

  state = {
    file: '',
    error: '',
    msg: ''
  }

  uploadFile = (event) => {
    event.preventDefault();
    this.setState({error: '', msg: ''});

    if(!this.state.file) {
      this.setState({error: 'Please upload a file.'})
      return;
    }

    if(this.state.file.size >= 2000000) {
      this.setState({error: 'File size exceeds limit of 2MB.'})
      return;
    }

    let data = new FormData();
    data.append('file', this.state.file);
    data.append('name', this.state.file.name);

    fetch('http://localhost:8181/api/files', {
      method: 'POST',
      body: data
    }).then(response => {
      this.setState({error: '', msg: 'Sucessfully uploaded file'});
    }).catch(err => {
      this.setState({error: err});
    });

  }

  downloadRandomImage = () => {
    fetch('http://localhost:8181/api/files')
      .then(response => {
        const filename =  response.headers.get('Content-Disposition').split('filename=')[1];
        response.blob().then(blob => {
          let url=  window.URL.createObjectURL(blob);
          let a = document.createElement('a');
          a.href = url;
          a.download = filename;
          a.click();
        });
    });
  }

  onFileChange = (event) => {
    this.setState({
      file: event.target.files[0]
    });
  }

  render() {
    return (
      <div className="container">

        <div className="border border-danger rounded element2">
          <h3>Upload a file</h3>
          <h4 style={{color: 'red'}}>{this.state.error}</h4>
          <h4 style={{color: 'green'}}>{this.state.msg}</h4>
          <input class="btn btn-outline-danger" onChange={this.onFileChange} type="file"></input>
          <button type="button" class="btn btn-outline-danger ml-2" onClick={this.uploadFile}>Upload</button>
        </div>



        <div className="border border-danger rounded element2">
          <h3>Download a random file</h3>
          <button type="button" class="btn btn-outline-danger" onClick={this.downloadRandomImage}>Download</button>
        </div>
      </div>
    );
  }
}

export default Files;
