# PIA - zabran

Docker se nepodařilo na Windows otestovat s Docker-compose, ale soubory Dockerfile i Docker-compose by měly být funkční. Ve Windows jsou s Docker Toolbox podle všeho už tři roky problémy a nejde tam jednoduchým způsobem nastavit cesta u volume z místa na fyzickém disku.

#Role: 

USER - přihlášený uživatel, který nemá žádnou jinou roli. (O statními se vzájemně vylučuje. Ostatní role můžou být zároveň.)

ADMIN - admin (username: 'admin' heslo: 'default')

SECRETARY - jednatel (username: 'sec' heslo: 'sec')

ACCOUNTANT - účetní (username: 'acc' heslo: 'acc')

#Entity:

Invoice (faktura) ukládá data company (společnosti) - je možné je vybrat. Ukládá však pouze jejich data.

Commodity se používá čistě pro ceník - není možné přidat nové commodity do databáze.

Item je položkou invoice (počet, cena za kus s DPH a název). Původně měla být embeddet, ale thymeleaf s tím nedokázal pracovat a bylo třeba to řešit oboustrannou 1:n závislostí.

#Detaily:

Při editaci může být heslo necháno prázdné - pak se ponechává heslo staré.

Invoice controller je mírně divoký (obsahuje 1 verzi pro vytvoření a 1 pro prohlížení/editaci. Obě verze mají vytvoření stránky, přidání a odebrání řádky a submit).

Komentáře jsem ve většině míst vynechal, funkčnost vychází z názvů metod a parametrů.

#Vyřešeno:

Nevyřešený přesný formát faktury - nikdy jsem nedělal nic v ekonomii, takže beru to spíš jako maketu. Faktura se nikde "netiskne", je jen uložená se všemi potřebnými údaji.

Nevyřešená error page. - postupoval jsem podle návodů, vypnout white label page se mi podařilo, ale na errorpage to stejně nepřesměrovávalo.

Všechny tři bonusové funkčnosti byly implementovány.
