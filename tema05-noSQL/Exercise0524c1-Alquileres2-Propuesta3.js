//David Oscar Bohez

use taller2;

db.vehiculos.save({
    matricula:'DAB 5932',
    marca:'Fiat',
    modelo:'Ducato',
    grupo:'C',
    precio: 31,
    carga: 7500,
    alquiler: [
        {
        fechasalida:new Date('2018-01-01 10:10:10'),
        fechaprevista:new Date('2019-01-01 10:10:10'),
        kminiciales: 2455,
        importe: 30,
        dni:'H1819321H',
        nombre:'juan',
        apellidos:'lopez',
        direccion:' calle vista verdosa'
        }
    ]
});

db.vehiculos.save({
    matricula:'DBZ 5432',
    marca:'Fiat',
    modelo:'500',
    grupo:'A',
    precio:21.58,
    alquiler: [
        {
        fechasalida:new Date('2018-01-01 10:10:10'),
        fechaprevista:new Date('2019-01-01 10:10:10'),
        fechadevolucion:new Date('2019-01-01 10:10:10'),
        kminiciales: 2455,
        kmfinales: 3000,
        importe: 30,
        dni:'H1819321H',
        nombre:'juan',
        apellidos:'lopez',
        direccion:' calle vista verdosa'
        }
    ]
});

db.vehiculos.save({
    matricula:'PEN 1111',
    marca:'mercedes',
    modelo:'elCaro',
    grupo:'B',
    precio:121,
    alquiler: [
        {
        fechasalida:new Date('2018-01-01 10:10:10'),
        fechaprevista:new Date('2019-01-01 10:10:10'),
        fechadevolucion:new Date('2019-01-01 10:10:10'),
        kminiciales: 2455,
        kmfinales: 3000,
        importe: 30,
        dni:'L7352094G',
        nombre:'juan',
        apellidos:'lopez',
        direccion:' calle vista azulosa'
        }
    ]
});

