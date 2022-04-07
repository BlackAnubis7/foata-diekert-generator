import scala.collection.mutable

object Util {
  /**
   * Dodaje element do mapy
   *
   * @param map   mapa do zmodyfikowania
   * @param tuple krotka (Klucz, Wartość)
   * @tparam A typ klucza
   * @tparam B typ wartości
   * @return nowa mapa, z dodanym odwzorowaniem klucz -> wartość
   */
  def addToMap[A, B](map: Map[A, B], tuple: (A, B)): Map[A, B] =
    map ++ Map(tuple._1 -> tuple._2)

  /**
   * Dodaje element do mapy
   *
   * @param map   mapa do zmodyfikowania
   * @param key   klucz
   * @param value wartość
   * @tparam A typ klucza
   * @tparam B typ wartości
   * @return nowa mapa, z dodanym odwzorowaniem klucz -> wartość
   */
  def addToMap[A, B](map: Map[A, B], key: A, value: B): Map[A, B] =
    addToMap(map, (key, value))

  /**
   * Łączy tablicę kluczy z tablicą wartości
   * @param labels tablica kluczy
   * @param prods tablica wartości
   * @tparam A typ klucza
   * @tparam B typ wartości
   * @return mapa z odwzorowaniami klucz -> wartość
   */
  def label[A, B](labels: Array[A], prods: Array[B]): Map[A, B] =
    labels.zip(prods).foldLeft(Map[A, B]())((m, kv) => addToMap(m, kv))

  /**
   * Generuje nieuporządkowane pary elementów tablicy, pilnując żeby dana para występowała tylko raz
   * @param array tablica z elementami do sparowania
   * @tparam T typ elementów tablicy
   * @return tablica krotek, zawierających pary elementów
   */
  def genPairs[T](array: Array[T]): Array[(T, T)] =
    array.foldLeft(Array[(T, T)]())(
      (a: Array[(T, T)], b: T) => a ++ array.foldLeft(Array[(T, T)]())(
        (c: Array[(T, T)], d: T) => c :+ (b, d)
      )
    ).distinct.filter(t => array.indexOf(t._1) <= array.indexOf(t._2))
}
