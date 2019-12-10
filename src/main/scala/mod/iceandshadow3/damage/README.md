# .damage

Classes for IaS3's altered damage system. See the wiki for more info on the damage mechanics themselves.

Classes in `damage.impl` are not bound by the usual type referencing rules.

## How to hurt something

Instantiate `Attack`, then apply it to a `WEntity`.
It will take some configuration options, along with a sequence of `BDamage` instances.
These instances should extend the desired `TDmgType`.

Example:
```scala
//Create a tickle curse that does sharp damage from the tickle god's fingers.
val tickles = Attack(tickleGod, tickleGodFingers, "tickles", AttackForm.CURSE, Damage(92.0) with TDmgTypeSharp)
//"Tickle" someone.
tickles(victim)
```

Note that the string argument dictates the attack's death message, which (before localization) will be
`death.iceandshadow3.$NAME`.