var  PartyListViewRowComponent= React.createClass({
  componentDidMount: function () {
    $(this.refs.delete).tooltip();
    $(this.refs.edit).tooltip();
    $(this.refs.info).tooltip();
  },
  handleDetailsClick: function(id){
    var self = this;
    return function() {
      self.context.router.push('admin/party/' + id);
    };
  },
  render: function() {
    return (
            <tr>
              <td className="small">
                {this.props.partyNumber}
              </td>
              <td className="small">
                {this.props.name}
              </td>
              <td className="small">
                <button onClick={this.handleDetailsClick(this.props.id)} ref="info" title="Detaliau" id={'details-button-' + this.props.id} className='btn btn-info btn-sm fa fa-info' role='button'></button>
                &nbsp;
                &nbsp;
                <a href={'#/admin/party/edit/' + this.props.id} data-toggle="tooltip2" id={'edit-button-' + this.props.id} title="Atnaujinti Partijos informaciją" type="button" className="btn btn-primary btn-sm fa fa-pencil"></a>
              </td>
            </tr>
    );
  }
});
PartyListViewRowComponent.contextTypes = {
  router: React.PropTypes.object.isRequired,
};
window.PartyListViewRowComponent = PartyListViewRowComponent;