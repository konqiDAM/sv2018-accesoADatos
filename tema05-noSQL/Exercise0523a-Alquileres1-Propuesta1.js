//RaulGogna
use vehiculos
db.alquileres.save({matricula: "", marca: "Fiat", modelo: 500, grupo: "", precioDia: 21.58});
db.alquileres.save({matricula: "5487CMS", marca: "Nissan", modelo: "Micra", grupo: "A", precioDia: 21.58, extras: [{extras: "4 ruedas"}]});
db.alquileres.save({matricula: "1234AVS", marca: "Opel", modelo: "Corsa", grupo: "B", precioDia: 30.5, extras:[{extra: "automático"}, {extra: "traccion 4 ruedas"}]);
db.alquileres.save({matricula: "1234BFD", marca: "Opel", modelo: "Astra", grupo: "B", precioDia: 30.5, extras: [{extra: "automático"}, {extra: "traccion delantera"}]});
db.alquileres.save({matricula: "3421DCS", marca: "Fiat", modelo: "Ducato", grupo: "A", precioDia: 28.45, volCargaLitros: 7500});
db.alquileres.find({$where: "this.precioDia < 40"});
db.clientes.save({dni: "48776503N", nombre: "Raul", apellidos: "Gogna", direccion: [{linea: "Lillo Juan, 128"},{linea: "03690 San Vicente del Raspeig"}]});
db.clientes.save({dni: "87784565M", nombre: "JOrge", apellidos: "Saez", direccion: [{linea: "Lillo Juan, 118"},{linea: "03690 San Vicente del Raspeig"}, {linea: "Alcante"}]});
db.clientes.find({$where: "this.direccion = '03690 San Vicente del Raspeig'"});
db.clientes.find({direccion: null});
db.clientes.deleteOne({direccion: null});