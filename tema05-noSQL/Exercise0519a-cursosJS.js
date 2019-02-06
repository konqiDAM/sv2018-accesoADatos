//Álvaro Monllor Quesada
use cursos
db.cursos.save(
    {
        nombre: 'Monogame',
        codigo: 1, comienzo: new Date('2019-02-07'), 
        contenidos: [
            {contenido: 'Origen: XNA'},
            {contenido: 'El bucle de juego'}, 
            {contenido: 'Assets'}, 
            {contenido: 'Acciones del usuario'}, 
            {contenido: 'Varias pantallas'}
            ]
    }
)