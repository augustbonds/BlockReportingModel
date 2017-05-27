if [ $# -eq 0 ]
  then
    echo "usage: ./generateandview.sh <graph engine>"
    exit 1
fi

case $1 in 
  dot|neato|twopi)
    echo "Valid graph engine" ;;
  *)
    echo "Not a valid graph engine"
    exit 1 ;;
esac

file=replica_states.dot

dot -Tpng -O $file

feh $file.png
