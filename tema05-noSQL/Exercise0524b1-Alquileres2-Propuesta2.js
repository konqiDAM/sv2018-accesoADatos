//Adrián Fernández Arnal

use alquileres2;

db.vehiculos.save(
    [
        {
            matricula:"12345678A",
            marca:"Fiat",
            modelo:"500",
            grupo:"A",
            precioDia:21.58,
            extras:[
                {nombre:"Tracción a las 4 ruedas"},
                {nombre:"Cambio automático"}
            ]
        },
        {
            matricula:"12345678B",
            marca:"Fiat",
            modelo:"Ducato",
            grupo:"B",
            precioDia:46.58,
            volumenCarga:7500
        }
    ]
);

db.vehiculos.find(
    {
        precioDia:{$lt:40}
    }
);

db.vehiculos.update(
    {matricula:"12345678A"},
    {
        $addToSet: {
            "alquileres":
            {
                fechaSalida:ISODate("2019-02-21 12:43:22"),
                fechaDevolucionPrevista:ISODate("2019-02-23 09:43:22"),
                fechaDevolucionReal:ISODate("2019-02-24 10:00:12"),
                kmIniciales:200.5,
                kmFinales:324.24,
                importe:345.99,
                cliente:{
                    dni:"1234A",
                    nombre:"Victor",
                    apellidos:"Tebapegasín Juan",
                    direccion:"San Vicente del Raspeig - Pintor Otillo 32"
                }
            }
        }
    }
);

db.vehiculos.update(
    {matricula:"12345678A"},
    {
        $addToSet: {
            "alquileres":
            {
                fechaSalida:ISODate("2019-02-21 12:43:22"),
                fechaDevolucionPrevista:ISODate("2019-02-22 12:23:11"),
                kmIniciales:167.2,
                importe:242.94,
                cliente:{
                    dni:"1234B",
                    nombre:"Suneo",
                    apellidos:"Arogayaki",
                    direccion:"Alicante - Pinturas veinticinco"
                }
            }
        }
    }
);

db.vehiculos.update(
    {matricula:"12345678B"},
    {
        $addToSet:{
            "alquileres":{
                fechaSalida:ISODate("2019-02-10 06:43:22"),
                fechaDevolucionPrevista:ISODate("2019-02-12 08:43:22"),
                fechaDevolucionReal:ISODate("2019-02-12 08:43:22"),
                kmIniciales:100,
                kmFinales:208.2,
                importe:166.99,
                cliente:{
                    dni:"1234B",
                    nombre:"Suneo",
                    apellidos:"Arogayaki",
                    direccion:"Alicante - Pinturas veinticinco"
                }
            }
        }
    }
);
