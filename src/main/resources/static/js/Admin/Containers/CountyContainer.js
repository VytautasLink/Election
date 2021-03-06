var fileErrorMesages = [];
var deleteErrorMessages = [];
function getCounty(self) {
  axios
    .get('/county')
    .then(function(response){
      self.setState({
        countyList :  response.data,
        isLoading : false,
      });
      EventEmitter.publish({ eventType: 'LogCounty' });
    })
    .catch(function(err){
      console.error('CountyContainer.getCounty.axios.get.county', err);
    });
}
function validate(self, code){
  if (code == 415) {fileErrorMesages.push('Netinkamas CSV failas, stulpelių skaičius skiriasi');}
  if (code == 422) {fileErrorMesages.push('Blogi CSV duomenys');}
  if (code == 424) {fileErrorMesages.push('Blogi CSV duomenys, yra kandidatu, kurie jau užregistruoti');}
  if (code == 426) {fileErrorMesages.push('Blogi CSV duomenys, kandidatai jau priskirti apygardai prieš keldami ištrinkite esanti kandidatų sąrašą');}
  if (code == 451) {fileErrorMesages.push('Kandidatas priskiriamas partijai, kuri neregistruota');}
  if (code == 428) {deleteErrorMessages.push('Šitos apygardos kandidatamas yra užregistruotų balsų, trinti tokią apygardą draudžiama');}
  self.setState({
    deleteErrorMessages : deleteErrorMessages,
    fileErrorMesages : fileErrorMesages,
  });
}
var CountyContainer = React.createClass({
  getInitialState: function() {
    return {
      countyList : [],
      isLoading : true,
      succesCreateText : '',
      countyId : null,
      file : null,
      fileErrorMesages : [],
      succesMessage : '',
      deletedCountyName : '',
      deleteErrorMessages : [],
    };
  },
  componentWillMount: function() {
    if (this.props.location.query.succesCreateText != null) {
      this.setState({succesCreateText : this.props.location.query.succesCreateText});
    }
    getCounty(this);
    this.setState({fileErrorMesages : [], deleteErrorMessages : [],});
  },
  componentDidMount: function() {
    if (this.props.location.query.succesCreateText != null) {
      this.setState({succesCreateText : this.props.location.query.succesCreateText});
    }
    getCounty(this);
  },
  componentWillReceiveProps: function(nextProps) {
    if (this.props.location.query.succesCreateText != null) {
      this.setState({succesCreateText : this.props.location.query.succesCreateText});
    }
    getCounty(this);
  },
  onHandleFormAddSingleCandSubmit : function(event){
    event.preventDefault();
    this.setState({fileErrorMesages : [], succesMessage : ''});
    fileErrorMesages = [];
    var self = this;
    //uploadfile constructing data
    var file = 'nofile.aaa';
    if (this.state.file != null) {
      file = this.state.file;}
    var data = new FormData();
    var header = { headers: {
      'Content-Type': 'multipart/form-data',
      'County-Id': this.state.countyId} };
    data.append('file', file);
    //validation
    console.log(file);
    if(!validation.checkForCsv(file.name)) {fileErrorMesages.push('Būtina prisegti *.csv formato failą');}
    if (fileErrorMesages.length == 0) {
      axios
      .put('/county', data, header)
      .then(function(response){
        getCounty(self);
        self.setState({succesMessage : 'Apygardai sėkmingai įkeltas vienmandatės apygardos sąrašas'});
      })
      .catch(function (error) {
        if (error.response) {
          validate(self, error.response.status);
        }
      });
    }
    else {
      this.setState({fileErrorMesages : fileErrorMesages});
    }
  },
  onHandleDelete: function(id, name) {
    deleteErrorMessages =[];
    var self = this;
    axios
      .delete('/county/'+ id)
      .then(function(response){
        self.setState({succesCreateText : '' , deletedCountyName : 'Apygarda ' + name + ' ištrinta'});
        getCounty(self);
      })
      .catch(function(error){
        if (error.response) {
          validate(self, error.response.status);
        }
        console.error('CountyContainer.onHandleDelete.axios', err);
      });
  },

  onFileChange : function(file, id){
    this.setState({
      countyId : id,
      file : file,
    });
  },
  onHandleAddClick : function(){
    this.setState({fileErrorMesages : []});
  },

  render: function() {
    document.title='Administratoriaus Apygardų Vaizdas Rinkimai 2017';
    if (this.state.isLoading) {
      return (
        <div>
          <img src='./Images/loading.gif'/>
        </div>
      );
    } else {
      return (
        <CountyListViewComponent
          countyList={this.state.countyList}
          onHandleDelete={this.onHandleDelete}
          succesCreateText={this.state.succesCreateText}
          onFileChange={this.onFileChange}
          fileErrorMesages={this.state.fileErrorMesages}
          onHandleFormAddSingleCandSubmit={this.onHandleFormAddSingleCandSubmit}
          onHandleAddClick={this.onHandleAddClick}
          succesMessage={this.state.succesMessage}
          deletedCountyName={this.state.deletedCountyName}
          deleteErrorMessages={this.state.deleteErrorMessages}
        />
      );
    }
  }
});

CountyContainer.contextTypes = {
  router: React.PropTypes.object.isRequired,
};

window.CountyContainer = CountyContainer;
