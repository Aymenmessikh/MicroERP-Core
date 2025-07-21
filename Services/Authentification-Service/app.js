var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var cors = require('cors');  // Import CORS package

var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');

var app = express();

// CORS configuration (allowing requests from Angular's development server)
const corsOptions = {
  origin: 'http://localhost:4200',  // Allow requests from Angular app
  methods: 'GET,POST,PUT,DELETE',  // Allowed HTTP methods
  allowedHeaders: 'Content-Type,Authorization',  // Allowed headers
};

// Use CORS middleware
app.use(cors(corsOptions));

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'pug');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/users', usersRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;

const Eureka = require('eureka-js-client').Eureka;

const client = new Eureka({
  instance: {
    app: 'Authetification-Service', // Nom du service (doit être unique)
    hostName: 'localhost',
    ipAddr: '127.0.0.1',
    port: {
      '$': 3000, // Port de votre service Express
      '@enabled': true,
    },
    vipAddress: 'Authetification-Service',
    dataCenterInfo: {
      '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
      name: 'MyOwn',
    },
  },
  eureka: {
    host: 'localhost', // Adresse du serveur Eureka
    port: 8761,
    servicePath: '/eureka/apps/',
  },
});

client.start(error => {
  console.log(error || 'Service Express.js enregistré sur Eureka');
});

// Gestion de la fermeture propre
process.on('SIGINT', () => {
  client.stop();
  process.exit();
});