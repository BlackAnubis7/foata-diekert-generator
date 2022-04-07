object Dependencies {
  /**
   * Sprawdza zależność produkcji
   * @param p1 produkcja
   * @param p2 produkcja
   * @return czy produkcje są od siebie zależne
   */
  def isD(p1: String, p2: String): Boolean =
    p1.contains(p2(0)) ||
      p2.contains(p1(0))

  /**
   * Sprawdza zależność produkcji
   * @param map mapa z odwzorowaniem (Znak z alfabetu -> Produkcja)
   * @param key1 znak z alfabetu odpowiadający produkcji z `map`
   * @param key2 znak z alfabetu odpowiadający produkcji z `map`
   * @tparam T typ znaków alfabetu
   * @return czy produkcje są od siebie zależne
   */
  def isD[T](map: Map[T, String], key1: T, key2: T): Boolean =
    isD(map(key1), map(key2))

  /**
   * Sprawdza zależność produkcji
   * @param map mapa z odwzorowaniem (Znak z alfabetu -> Produkcja)
   * @param keys krotka znaków z alfabetu odpowiadającym produkcji z `map`
   * @tparam T typ znaków alfabetu
   * @return czy produkcje są od siebie zależne
   */
  def isD[T](map: Map[T, String], keys: (T, T)): Boolean =
    isD(map(keys._1), map(keys._2))

//  def isD(p1: String, p2: String): Boolean =
//    p1.split(" ").contains(p2.split(" ")(0)) ||
//      p2.split(" ").contains(p1.split(" ")(0))
}
