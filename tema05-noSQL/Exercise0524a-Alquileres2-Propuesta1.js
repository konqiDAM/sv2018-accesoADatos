//Jairo Pérez Sánchez

use alquiler2;

db.vehiculo.save(
    [
        {matricula:"1111 AAA",
        marca:"Fiat",
        modelo:"500",
        grupo:"A",
        precio:21.58,
        extras:[
            {extra:"Turbo"},
            {extra:"Tapeceria de cuero"}
        ]},
        {matricula:"2222 BBB",
        marca:"Fiat",
        modelo:"Ducato",
        grupo:"B",
        precio:53.22,
        volumen:7500},
        {matricula:"3333 CCC",
        marca:"Kia",
        modelo:"Sportage",
        grupo:"C",
        precio:36.49}
    ]
);

db.vehiculo.find(
    {precio:{$lt:40}}
);

db.vehiculo.update(
    {matricula:"1111 AAA"},
    {$set:{
        alquileres:[
            {fechaSalida:ISODate("2019-01-03T22:30:00Z"),
            fechaPreDevolucion:ISODate("2019-03-03T22:30:00Z"),
            kmIniciales:1200,
            importe:956.45,
            dniCliente:"1111A",
            nombre:"Jairo",
            apellidos:"Pérez Sánchez",
            direccion:"C/13 215, San Vicente"
            },
            {fechaSalida:ISODate("2018-02-03T22:30:00Z"),
            fechaPreDevolucion:ISODate("2018-03-03T22:30:00Z"),
            fechaDevolucion:ISODate("2018-03-03T22:30:00Z"),
            kmIniciales:1200,
            kmFinales:2000,
            importe:956.45,
            dniCliente:"2222A",
            nombre:"Nacho",
            apellidos:"Cabanes",
            direccion:"C/14 211, San Vicente"
            }
        ]
    }}
);

db.vehiculo.update(
    {matricula:"2222 BBB"},
    {$set:{
        alquileres:[
            {fechaSalida:ISODate("2019-01-03T22:30:00Z"),
            fechaPreDevolucion:ISODate("2019-03-03T22:30:00Z"),
            kmIniciales:1200,
            importe:956.45,
            dniCliente:"1111A",
            nombre:"Jairo",
            apellidos:"Pérez Sánchez",
            direccion:"C/13 215, San Vicente"
            }
        ]
    }}
);
