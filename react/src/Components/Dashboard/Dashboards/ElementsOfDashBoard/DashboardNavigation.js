import React from 'react';
import {Link} from 'react-router-dom';
import '../../../../App.css'


const DashboardNavigation = (props) => {

    return (
        <React.Fragment>
                   <div className='col-lg-2'>
                        <Link className="btn btn-lg btn-block buttonTransparent" to=
                        {"/dashboard/documents/all/"}>
                        Visi dokumentai
                        </Link>
                    </div>
                    <div className='col-lg-2'> 
                        <Link className="btn btn-lg btn-block buttonTransparent" to=
                        {"/dashboard/documents/created/"}>
                        {/* Submitted documents */}
                        Sukurti dokumentai
                        </Link>
                    </div>
                    <div className='col-lg-2'> 
                        <Link className="btn btn-lg btn-block buttonTransparent" to=
                        {"/dashboard/documents/submitted/"}>
                        {/* Submitted documents */}
                        Pateikti dokumentai
                        </Link>
                    </div>
                    <div className='col-lg-2'> 
                        <Link className="btn btn-lg btn-block buttonTransparent" to=
                        {"/dashboard/documents/approved/"}>
                        {/* Approved documents */}
                        Patvirtinti dokumentai
                        </Link>
                    </div>
                    <div className='col-lg-2'> 
                        <Link className="btn btn-lg btn-block buttonTransparent" to=
                        {"/dashboard/documents/rejected/"}>
                        {/* Rejected documents */}
                        Atmesti dokumentai
                        </Link>
                    </div>
                    <div className='col-lg-2'> 
                        <Link className="btn btn-lg btn-block buttonTransparent" to=
                        {"/dashboard/documents/to_aproove/"}>
                        {/* Rejected documents */}
                        Dokumentai tvirtinimui
                        </Link>
                    </div>

        </React.Fragment>
    );
};

export default DashboardNavigation;
