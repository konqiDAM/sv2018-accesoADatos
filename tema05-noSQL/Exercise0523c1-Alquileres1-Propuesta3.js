//David Oscar Bohez

use taller;

db.vehiculos.save({
    matricula:'DBZ 5432',
    marca:'Fiat',
    modelo:'500',
    grupo:'A',
    precio:21.58
});

db.vehiculos.save({
    matricula:'DAB 5932',
    marca:'Fiat',
    modelo:'Ducato',
    grupo:'C',
    precio: 31,
    carga: 7500
});
db.vehiculos.save({
    matricula:'PEN 1111',
    marca:'mercedes',
    modelo:'elCaro',
    grupo:'B',
    precio:121,
    extras: [
    {nombre:'cosas hermosas'},
    {nombre:'airbag'}
    ]
});

db.vehiculos.find({precio:{$lt:40}});

db.clientes.save({
    dni:'H1819321H',
    nombre:'juan',
    apellidos:'etes',
    direccion:[
    {nombre:'Lillo Juan, 128'},
    {nombre:'03690 San Vicente del Raspeig'}
    ]
});

db.clientes.save({
    dni:'P9731564B',
    nombre:'lolo',
    apellidos:'ve',
    direccion:[
    {nombre:'Lillo Juan, 128'}
    ]
});

db.clientes.save({
    dni:'J12873304G',
    nombre:'mario',
    apellidos:'verde'
});

db.clientes.find({direccion:{nombre:'03690 San Vicente del Raspeig'}});

db.clientes.find( { direccion: { $exists: false } } )

db.clientes.remove({dni:'J12873304G'});

db.alquiler.save({
    vehiculo:'DBZ 5432',
    cliente:'H1819321H',
    fechasalida:new Date('2018-01-01 10:10:10'),
    fechadevolucion:new Date('2018-02-01 12:10:10'),
    kminiciales:121,
    kmfinales:2222 ,
    importe: 34,
    incidencias: [
    {nombre:'pintura rallada'}
    ]
    
});

db.alquiler.save({
    vehiculo:'DBZ 5432',
    cliente:'P9731564B',
    fechasalida:new Date('2019-01-01 14:20:10'),
    kminiciales:121,
    importe: 34
});

db.alquiler.update(
   {vehiculo:'DBZ 5432',cliente:'P9731564B'},
   {
      $set:{ 
           incidencias:[{nombre:"faro roto"}],
           fechadevolucion:new Date('2018-02-01 12:10:10'),
           kmfinales: 455 
      }
   }
);

db.alquiler.save({
    vehiculo:'PEN 1111',
    cliente:'P9731564B',
    fechasalida:new Date('2020-01-01 16:20:10'),
    kminiciales:121,
    importe: 360
});


