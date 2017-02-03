﻿var DistrictListViewRowComponent = React.createClass({
  componentDidMount: function () {
    $(this.refs.delete).tooltip();
    $(this.refs.edit).tooltip();
  },
  onHandleDeleteClick : function(){
    this.props.onHandleDelete(this.props.id);
  } ,
  onHandleUploadCanClick : function(){
    this.props.onHandleDelete({

    });
  } ,
  render: function() {
    return (
            <tr>
              <td className="small">
                {this.props.id}
              </td>
              <td className="small">
                {this.props.name}
              </td>
              <td className="small">
                {this.props.adress}
              </td>
              <td className="small">
                {this.props.countyName}
              </td>
              <td className="small">
                {this.props.representativeName}
              </td>
              <td>
                <div>
                &nbsp;
                <a href={'#/admin/district/edit/' + this.props.id} ref="edit" data-toggle="tooltip2" title="Redaguoti" id={'edit-district-' + this.props.id} type="button " className="btn btn-primary btn-sm fa fa-pencil"></a>
                &nbsp;
                <button onClick={this.onHandleDeleteClick} ref="delete" data-toggle="tooltip1" title="Ištrinti" id={'delete-district-' + this.props.id} type="button" className="btn btn-danger btn-sm fa fa-trash"></button>
               </div>
              </td>
            </tr>
    );
  }
});

window.DistrictListViewRowComponent = DistrictListViewRowComponent;