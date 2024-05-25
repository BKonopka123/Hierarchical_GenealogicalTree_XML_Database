USE HXML;

CREATE TABLE DrzewoGenealogiczne(
    DrzewoGenealogiczneID int IDENTITY(1,1),
    XDocumentDrzewoGenealogiczne xml,
    CONSTRAINT PK_DrzewoGenealogiczne PRIMARY KEY (DrzewoGenealogiczneID)
);

declare @DrzewoGenealogiczneXML xml;
set @DrzewoGenealogiczneXML = '<?xml version="1.0"?>
    <Osoba>
        <Osoba_id> 1 </Osoba_id>
        <Imie> Piotr </Imie>
        <Nazwisko> Kowalski </Nazwisko>
        <Plec> Mezczyzna </Plec>
        <Ojciec_id> null </Ojciec_id>
        <Matka_id> null </Matka_id>
        <Urodziny> 1942-10-9 </Urodziny>
        <Smierc> 2002-11-3 </Smierc>
    </Osoba>
    <Osoba>
        <Osoba_id> 2 </Osoba_id>
        <Imie> Anna </Imie>
        <Nazwisko> Kowalska </Nazwisko>
        <Plec> Kobieta </Plec>
        <Ojciec_id> null </Ojciec_id>
        <Matka_id> null </Matka_id>
        <Urodziny> 1944-03-12 </Urodziny>
        <Smierc> 2000-04-15 </Smierc>
    </Osoba>
    <Osoba>
        <Osoba_id> 3 </Osoba_id>
        <Imie> Tomasz </Imie>
        <Nazwisko> Adamczyk </Nazwisko>
        <Plec> Mezczyzna </Plec>
        <Ojciec_id> null </Ojciec_id>
        <Matka_id> null </Matka_id>
        <Urodziny> 1950-01-09 </Urodziny>
        <Smierc> 2015-03-09 </Smierc>
    </Osoba>
    <Osoba>
        <Osoba_id> 4 </Osoba_id>
        <Imie> Katarzyna </Imie>
        <Nazwisko> Adamczyk </Nazwisko>
        <Plec> Kobieta </Plec>
        <Ojciec_id> null </Ojciec_id>
        <Matka_id> null </Matka_id>
        <Urodziny> 1953-03-20 </Urodziny>
        <Smierc> null </Smierc>
    </Osoba>
    <Osoba>
        <Osoba_id> 5 </Osoba_id>
        <Imie> Marta </Imie>
        <Nazwisko> Kowalska </Nazwisko>
        <Plec> Kobieta </Plec>
        <Ojciec_id> 1 </Ojciec_id>
        <Matka_id> 2 </Matka_id>
        <Urodziny> 1972-06-24 </Urodziny>
        <Smierc> null </Smierc>
    </Osoba>
    <Osoba>
        <Osoba_id> 6 </Osoba_id>
        <Imie> Aleksandra </Imie>
        <Nazwisko> Kowalska </Nazwisko>
        <Plec> Kobieta </Plec>
        <Ojciec_id> 1 </Ojciec_id>
        <Matka_id> 2 </Matka_id>
        <Urodziny> 1974-07-12 </Urodziny>
        <Smierc> null </Smierc>
    </Osoba>
    <Osoba>
        <Osoba_id> 7 </Osoba_id>
        <Imie> Michal </Imie>
        <Nazwisko> Adamczyk </Nazwisko>
        <Plec> Mezczyzna </Plec>
        <Ojciec_id> 3 </Ojciec_id>
        <Matka_id> 4 </Matka_id>
        <Urodziny> 1975-02-10 </Urodziny>
        <Smierc> null </Smierc>
    </Osoba>
    <Osoba>
        <Osoba_id> 8 </Osoba_id>
        <Imie> Adam </Imie>
        <Nazwisko> Adamczyk </Nazwisko>
        <Plec> Mezczyzna </Plec>
        <Ojciec_id> 7 </Ojciec_id>
        <Matka_id> 6 </Matka_id>
        <Urodziny> 1997-06-27 </Urodziny>
        <Smierc> null </Smierc>
    </Osoba>';

INSERT INTO DrzewoGenealogiczne(XDocumentDrzewoGenealogiczne) VALUES (@DrzewoGenealogiczneXML);