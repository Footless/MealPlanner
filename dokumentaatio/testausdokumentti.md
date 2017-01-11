## Testauksesta ##

Sekä DatabaseSetup- että MealTweaker luokkaa on testattu eniten kaikista, vaikka niille ei testejä juuri olekaan. Luokkien testaaminen on vaikeaa, vähän eri syistä. 

MealTweaker käyttää osin satunnaisia arvoja, joten haluttujen tulosten saaminen on vaikeaa. Mahdollista se toki olisi, mutta vaatisi todella tarkkaa laskemista annetuista raaka-aineista ja määristä. Luokkaa onkin testattu todella paljon graafisen käyttöliittymän kautta kokeilemalla eri raaka-aineita ja makrojen määriä. Täydellinen se ei vielä ole (tuskin tulee koskaan olemaankaan, koska tuon tyyppisessä pyörityksessä on aina viilaamisen tarvetta), mutta toimii kuitenkin ja antaa järkeviä arvoja ulos.

DatabaseSetup luo tietokannan tarvittaessa, sekä lisää sinne taulut ja lisää tauluihin sisällön CSV-tiedostoista. Luokkaa testattiin sitä tehdessä pitkään ja hartaasti, kunnes se lopulta saatiin toimimaan halutulla tavalla. Taulujen täyttäminen makroilla tiedostosta jossa on ~250000 riviä ottaa 'hetken', joten en edes viitsinyt lähteä kokeilemaan mitä PIT:n mutantit tekevät sille ja koneelleni. Testailen sitä kuitenkin vielä käsin parissa eri ympäristössä, kunhan löydän Windowsin jostain.
