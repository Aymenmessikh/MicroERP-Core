export interface Page<T> {
    content: T[]; // Liste des éléments de la page
    totalElements: number; // Nombre total d'éléments
    totalPages: number; // Nombre total de pages
    size: number; // Nombre d'éléments par page
    number: number; // Numéro de la page actuelle (commençant à 0)
    first: boolean; // Indique si c'est la première page
    last: boolean; // Indique si c'est la dernière page
    numberOfElements: number; // Nombre d'éléments dans la page actuelle
    empty: boolean; // Indique si la page est vide
}
