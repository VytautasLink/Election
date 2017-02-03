var gs = window.generalStyles;

var DistrictRepresentativeComponent = React.createClass({
  render: function() {
    var succesCreateMessage = alerts.showSucces(this.props.succesCreateText);
    var array = [];
    this.props.representativesList.map(function(rep,index) {
      array.push(
        <DistrictRespesentativeListViewRowComponent
          id={rep.id}
          key={index}
          name={rep.name}
          surname={rep.surname}
          districtId={rep.districtId}
          districtName={rep.districtName}
        />
      );
    });
    return (

		      <div className="panel panel-default">
		        <div className="panel-heading" style={{paddingTop:20,paddingBottom:20}}>
		          <h4 style={{display:'inline'}}><i className="fa fa-table"></i>&nbsp; Apylinkės Atstovų sarašas</h4>
              <div className="text-success pull-right">
                <a href="#/admin/representative/create"  id="register-button"><i className="fa fa-plus"></i>
                &nbsp; Registruoti
                </a>
              </div>
		        </div>
		          <div className="panel-body">
                {succesCreateMessage}
		          </div>
		            <table className="table table-striped table-hover">
		              <thead>
		                <tr>
		                  <th>
		                    Nr
		                  </th>
		                  <th>
		                    Vardas
		                  </th>
		                  <th>
		                    Pavarde
		                  </th>
		                  <th>
		                    Atstovaujama Apylinkė
		                  </th>
		                </tr>
		              </thead>
		              <tbody>
		              {array}
		              </tbody>
		            </table>
              </div>
    );
  }
});

window.DistrictRepresentativeComponent = DistrictRepresentativeComponent;