import React from 'react';

const ModalError = (props) => {
    return (
        <div className="modal fade" id="modalError" role="dialog">
            <div className="modal-dialog modal-dialog-centered" role="document">
                <div className="modal-content">
                    <div className="modal-header alert-danger">
                        <h5 className="modal-title">Klaida!</h5>
                        <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div className="modal-body">
                        {props.errorText}
                    </div>
                    <div className="modal-footer">
                        <button type="button" className="btn btn-secondary" data-dismiss="modal">Uždaryti</button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ModalError;
